// ajuste o caminho conforme onde está o seu arquivo da função `requisicao`
import { requisicao } from '@/services/api';

const ENDPOINT = '/info-irriga';

/* ------------------------------------------------------------------ */
/* TIPOS                                                               */
/* ------------------------------------------------------------------ */

export type ModoIrrigacao = 'economica' | 'equilibrada' | 'customizada';

// Formato "limpo" usado pela tela (sem os nomes do back-end)
export type ConfiguracaoIrrigacao = {
    id : string;
  arduino: string;
  usuarioEmail: string;
  modo: ModoIrrigacao;
  umidadeMinima: number;
  intervaloIrrigacao: number;
  duracaoIrrigacao: number;
};

// Formato que o back-end envia/recebe
type DadosApi = {
    id:string;
  arduino?: string;
  configuacao: string; // (sic) mesmo nome que vem da API
  duracaoIrrigacao: number;
  intervaloIrrigacao: number;
  umidadeMinima: number;
  usuarioEmail: string;
};

type RespostaApi = {
  datas: DadosApi;
  message: string;
  status: string;
  sucess: boolean; // (sic)
};

// Resultado padronizado: a tela nunca precisa de try/catch
export type ResultadoServico<T> = {
  sucesso: boolean;
  mensagem: string;
  dados: T | null;
};

/* ------------------------------------------------------------------ */
/* CONVERSÃO DE MODO                                                   */
/* ------------------------------------------------------------------ */

// ATENÇÃO: confirme com o back-end como ele espera receber o modo
// (ex.: "ECONOMICA" sem acento). Só "CUSTOMIZADA" foi confirmado.
const MODO_PARA_API: Record<ModoIrrigacao, string> = {
  economica: 'ECONOMICA',
  equilibrada: 'EQUILIBRADA',
  customizada: 'CUSTOMIZADA',
};

// "CUSTOMIZADA" | "Econômica" | "equilibrada" -> ModoIrrigacao
export const modoDaApi = (valor: string): ModoIrrigacao => {
  const normalizado = (valor ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim();

  if (normalizado === 'economica') return 'economica';
  if (normalizado === 'equilibrada') return 'equilibrada';
  return 'customizada';
};

/* ------------------------------------------------------------------ */
/* GET /info-irriga                                                    */
/* ------------------------------------------------------------------ */

export async function buscarConfiguracao(): Promise<
  ResultadoServico<ConfiguracaoIrrigacao>
> {
  try {
    const { status, data } = await requisicao<RespostaApi>(ENDPOINT, 'GET');

    if (status !== 200 || !data?.sucess || !data.datas) {
      return {
        sucesso: false,
        mensagem: data?.message ?? 'Não foi possível carregar as informações.',
        dados: null,
      };
    }

    const info = data.datas;

    return {
      sucesso: true,
      mensagem: data.message,
      dados: {
        id:info.id,
        arduino: info.arduino ? info.arduino : "Não informado"  , 
        usuarioEmail: info.usuarioEmail,
        modo: modoDaApi(info.configuacao),
        umidadeMinima: info.umidadeMinima,
        intervaloIrrigacao: info.intervaloIrrigacao,
        duracaoIrrigacao: info.duracaoIrrigacao,
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

/* ------------------------------------------------------------------ */
/* POST /info-irriga                                                   */
/* ------------------------------------------------------------------ */

export async function salvarConfiguracao(
  config: ConfiguracaoIrrigacao
): Promise<ResultadoServico<null>> {
  // O body espelha o formato do GET. Se o back-end não precisar
  // de `arduino` e `usuarioEmail`, é só remover daqui.
  const body: DadosApi = {
    id: config.id,

    configuacao: MODO_PARA_API[config.modo],
    duracaoIrrigacao: config.duracaoIrrigacao,
    intervaloIrrigacao: config.intervaloIrrigacao,
    umidadeMinima: config.umidadeMinima,
    usuarioEmail: config.usuarioEmail,
  };

  try {
    const { status, data } = await requisicao<RespostaApi>(
      ENDPOINT,
      'PUT',
      body
    );

    const ok = status >= 200 && status < 300 && data?.sucess !== false;

    return {
      sucesso: ok,
      mensagem:
        data?.message ??
        (ok
          ? 'Configuração salva com sucesso.'
          : 'Não foi possível salvar a configuração.'),
      dados: null,
    };
  } catch {
    return {
      sucesso: false,
      mensagem: 'Falha de conexão com o servidor.',
      dados: null,
    };
  }
}