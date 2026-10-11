// ajuste o caminho conforme onde está o seu arquivo da função `requisicao`
import { requisicao } from "@/services/api"
// mesmo formato de resultado padronizado usado nos outros services
import type { ResultadoServico } from "@/screens/Home/api";

const ENDPOINT = '/consumo-agua';

export type ModoHistorico = 'Econômica' | 'Equilibrada' | 'Customizada';

// Formato "limpo" usado pela tela
export type HistoricoItem = {
  umidade: number;
  tempoLigadoMinutos: number;
  aguaLitros: number;
  modo: ModoHistorico | null; // a API pode enviar null
  dispositivo: string;
  data: string | null; // data ISO em UTC (ex.: "2026-10-10T22:29:37.989Z")
};

// Formato que o back-end envia
type ItemApi = {
  umidade: number;
  tempoLigado: number;
  aguaTotal: number;
  modo: string | null;
  dispositivo: string;
  data: string | null;
};

type RespostaApi = {
  datas: ItemApi[];
  message: string;
  status: string;
  sucess: boolean; // (sic) mesmo nome que vem da API
};

/* ------------------------------------------------------------------ */
/* CONVERSÕES                                                          */
/* ------------------------------------------------------------------ */

// "CUSTOMIZADA" -> "Customizada" | null -> null
function converterModo(valor: string | null): ModoHistorico | null {
  if (!valor) return null;

  const normalizado = valor
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim();

  if (normalizado === 'economica') return 'Econômica';
  if (normalizado === 'equilibrada') return 'Equilibrada';
  if (normalizado === 'customizada') return 'Customizada';

  return null;
}

/* ------------------------------------------------------------------ */
/* GET /consumo-agua                                                   */
/* ------------------------------------------------------------------ */

export async function buscarHistorico(): Promise<
  ResultadoServico<HistoricoItem[]>
> {
  try {
    const { status, data } = await requisicao<RespostaApi>(ENDPOINT, 'GET');

    if (status !== 200 || !data?.sucess || !Array.isArray(data.datas)) {
      return {
        sucesso: false,
        mensagem: data?.message ?? 'Não foi possível carregar o histórico.',
        dados: null,
      };
    }

    const itens: HistoricoItem[] = data.datas.map((item) => ({
      umidade: item.umidade,

      // ASSUMIDO: tempoLigado em MINUTOS
      tempoLigadoMinutos: item.tempoLigado,

      // ASSUMIDO: aguaTotal em LITROS
      aguaLitros: item.aguaTotal,

      modo: converterModo(item.modo),
      dispositivo: item.dispositivo,
      data: item.data ?? null,
    }));

    return {
      sucesso: true,
      mensagem: data.message,
      dados: itens,
    };
  } catch {
    return {
      sucesso: false,
      mensagem: 'Falha de conexão com o servidor.',
      dados: null,
    };
  }
}