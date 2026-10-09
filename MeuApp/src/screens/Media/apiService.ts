// ajuste o caminho conforme onde está o seu arquivo da função `requisicao`
import { requisicao } from "@/services/api";
// mesmo formato de resultado padronizado usado no irrigaService
import type { ResultadoServico } from "@/screens/Home/api";

const ENDPOINT = '/arduino/status';

/* ------------------------------------------------------------------ */
/* TIPOS                                                               */
/* ------------------------------------------------------------------ */

// Formato "limpo" usado pela tela
export type StatusArduino = {
  consumoAguaLitros: number;
  tempoLigadoMinutos: number;
  quantidadeIrrigacoes: number;
  ultimaIrrigacao: string | null; // data ISO (UTC)
  umidadeSolo: number;
};

// Formato que o back-end envia
type RespostaApi = {
  datas: {
    consumoAgua: {
      consumoTotal: number;
      tempoLigadoTotal: number;
      quantidadeVezesLigada: number;
      ultimaVezIrrigado: string | null;
    };
    umidadeSolo: number;
  };
  message: string;
  status: string;
  sucess: boolean; // (sic) mesmo nome que vem da API
};

/* ------------------------------------------------------------------ */
/* GET /arduino/status                                                 */
/* ------------------------------------------------------------------ */

export async function buscarStatusArduino(): Promise<
  ResultadoServico<StatusArduino>
> {
  try {
    const { status, data } = await requisicao<RespostaApi>(ENDPOINT, 'GET');

    if (status !== 200 || !data?.sucess || !data.datas) {
      return {
        sucesso: false,
        mensagem: data?.message ?? 'Não foi possível carregar o status.',
        dados: null,
      };
    }

    const { consumoAgua, umidadeSolo } = data.datas;

    return {
      sucesso: true,
      mensagem: data.message,
      dados: {
        // ASSUMIDO: consumoTotal em litros
        consumoAguaLitros: consumoAgua.consumoTotal,

        // ASSUMIDO: tempoLigadoTotal em MINUTOS (12.7 = 12min 42s).
        // Se o back-end enviar em horas, troque por:
        // consumoAgua.tempoLigadoTotal * 60
        tempoLigadoMinutos: consumoAgua.tempoLigadoTotal,

        quantidadeIrrigacoes: consumoAgua.quantidadeVezesLigada,
        ultimaIrrigacao: consumoAgua.ultimaVezIrrigado ?? null,
        umidadeSolo,
      },
    };
  } catch {
    return {
      sucesso: false,
      mensagem: 'Falha de conexão com o servidor.',
      dados: null,
    };
  }
}