import { useCallback, useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  Text,
  View,
} from 'react-native';

import { styles } from './styles';
// ajuste o caminho conforme onde você colocar o service
import {
  buscarStatusArduino,
  StatusArduino,
} from "@/screens/Media/apiService";

// De quanto em quanto tempo os dados são atualizados (ms)
const INTERVALO_ATUALIZACAO_MS = 10000;

export default function MediaScreen() {
  /* ------------------------ Dados da API ------------------------ */

  const [status, setStatus] = useState<StatusArduino | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  /* --------------------- Dados ainda simulados -------------------- */

  // A API ainda não envia a temperatura, então ela continua simulada.
  const [temperatura, setTemperatura] = useState(28.6);

  /*
   * SIMULAÇÃO DA TEMPERATURA
   *
   * FUTURAMENTE:
   * essa parte será substituída pelos dados recebidos
   * do Arduino.
   */
  useEffect(() => {
    const interval = setInterval(() => {
      setTemperatura((valorAtual) => {
        const variacao = Math.random() * 0.6 - 0.3;

        return Number((valorAtual + variacao).toFixed(1));
      });
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  /* ------------------ GET /arduino/status (service) ------------------ */

  const carregar = useCallback(async (primeiraCarga = false) => {
    if (primeiraCarga) {
      setCarregando(true);
    }

    const resultado = await buscarStatusArduino();

    if (!resultado.sucesso || !resultado.dados) {
      // Se já havia dados, eles continuam na tela (ver `offline` abaixo)
      setErro(resultado.mensagem);
    } else {
      setStatus(resultado.dados);
      setErro(null);
    }

    setCarregando(false);
  }, []);

  // Carrega ao abrir a tela e atualiza periodicamente
  useEffect(() => {
    carregar(true);

    const interval = setInterval(() => {
      carregar();
    }, INTERVALO_ATUALIZACAO_MS);

    return () => clearInterval(interval);
  }, [carregar]);

  const temDados = status !== null;
  const offline = temDados && erro !== null;

  /* --------------------------- Render --------------------------- */

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      {/* CABEÇALHO (sempre visível) */}

      <View style={styles.header}>
        <Text style={styles.title}>Monitoramento</Text>

        <Text style={styles.subtitle}>
          Acompanhe os dados do sistema de irrigação
        </Text>
      </View>

      {/* CARREGANDO */}

      {carregando && !temDados && (
        <View style={styles.card}>
          <ActivityIndicator size="large" color="#22C55E" />

          <Text
            style={[
              styles.cardSubtitle,
              { textAlign: 'center', marginTop: 14 },
            ]}
          >
            Carregando dados do sistema...
          </Text>
        </View>
      )}

      {/* ERRO (só na primeira carga, quando ainda não há dados) */}

      {!carregando && !temDados && erro && (
        <View style={styles.card}>
          <Text style={styles.cardTitle}>⚠️ Ops, algo deu errado</Text>

          <Text style={styles.cardSubtitle}>{erro}</Text>

          <Pressable
            style={{ marginTop: 16 }}
            onPress={() => carregar(true)}
          >
            <Text style={styles.statusText}>Tentar novamente</Text>
          </Pressable>
        </View>
      )}

      {/* CONTEÚDO */}

      {status && (
        <>
          {/* STATUS */}

          <View style={styles.status}>
            <View
              style={[
                styles.statusDot,
                offline && { backgroundColor: '#EF4444' },
              ]}
            />

            <Text
              style={[styles.statusText, offline && { color: '#EF4444' }]}
            >
              {offline
                ? 'Sem conexão, exibindo os últimos dados'
                : 'Dados em tempo real'}
            </Text>
          </View>

          {/* SENSORES */}

          <Text style={styles.sectionTitle}>Sensores</Text>

          <View style={styles.sensorContainer}>
            {/* Temperatura (simulada) */}

            <View style={styles.sensorCard}>
              <Text style={styles.sensorIcon}>🌡️</Text>

              <Text style={styles.sensorTitle}>Temperatura</Text>

              <View style={styles.sensorValueContainer}>
                <Text style={styles.sensorValue}>
                  {temperatura.toFixed(1)}
                </Text>

                <Text style={styles.sensorUnit}>°C</Text>
              </View>

              <Text style={styles.sensorStatus}>
                {getTemperatureStatus(temperatura)}
              </Text>
            </View>

            {/* Umidade (API) */}

            <View style={styles.sensorCard}>
              <Text style={styles.sensorIcon}>🌱</Text>

              <Text style={styles.sensorTitle}>Umidade do solo</Text>

              <View style={styles.sensorValueContainer}>
                <Text style={styles.sensorValue}>
                  {formatarNumero(status.umidadeSolo)}
                </Text>

                <Text style={styles.sensorUnit}>%</Text>
              </View>

              <Text style={styles.sensorStatus}>
                {getHumidityStatus(status.umidadeSolo)}
              </Text>
            </View>
          </View>

          {/* CONSUMO */}

          <Text style={styles.sectionTitle}>Consumo</Text>

          {/* Água (API) */}

          <View style={styles.card}>
            <View style={styles.cardHeader}>
              <Text style={styles.icon}>💧</Text>

              <View>
                <Text style={styles.cardTitle}>Consumo de água</Text>
                <Text style={styles.cardSubtitle}>Total acumulado</Text>
              </View>
            </View>

            <Text style={styles.value}>
              {formatarNumero(status.consumoAguaLitros)} L
            </Text>
          </View>

          {/* Energia (ainda sem dados na API, mantido como estava) */}

          <View style={styles.card}>
            <View style={styles.cardHeader}>
              <Text style={styles.icon}>⚡</Text>

              <View>
                <Text style={styles.cardTitle}>Consumo de energia</Text>
                <Text style={styles.cardSubtitle}>Este mês</Text>
              </View>
            </View>

            <Text style={styles.value}>18,4 kWh</Text>
          </View>

          {/* Tempo ligado (API) */}

          <View style={styles.card}>
            <View style={styles.cardHeader}>
              <Text style={styles.icon}>⏱️</Text>

              <View>
                <Text style={styles.cardTitle}>Tempo ligado</Text>
                <Text style={styles.cardSubtitle}>
                  Tempo total de funcionamento
                </Text>
              </View>
            </View>

            <Text style={styles.value}>
              {formatarTempo(status.tempoLigadoMinutos)}
            </Text>
          </View>

          {/* VIDA ÚTIL */}

          <View style={styles.lifeCard}>
            <Text style={styles.lifeTitle}>⚙️ Vida útil do sistema</Text>

            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Tempo de funcionamento</Text>

              <Text style={styles.infoValue}>
                {formatarTempo(status.tempoLigadoMinutos)}
              </Text>
            </View>

            <View style={styles.separator} />

            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Irrigações realizadas</Text>

              <Text style={styles.infoValue}>
                {status.quantidadeIrrigacoes}
              </Text>
            </View>

            <View style={styles.separator} />

            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Última irrigação</Text>

              <Text style={styles.infoValue}>
                {formatarUltimaIrrigacao(status.ultimaIrrigacao)}
              </Text>
            </View>
          </View>
        </>
      )}
    </ScrollView>
  );
}

/* ------------------------------------------------------------------ */
/* FORMATADORES                                                        */
/* ------------------------------------------------------------------ */

const pad = (n: number) => String(n).padStart(2, '0');

/*
 * 19.7 -> "19,7" | 60 -> "60"
 */
function formatarNumero(valor: number) {
  return String(Number(valor.toFixed(1))).replace('.', ',');
}

/*
 * Recebe minutos (pode ser decimal).
 * 12.7 -> "12min 42s" | 125.5 -> "2h 05min"
 */
function formatarTempo(minutos: number) {
  const totalSegundos = Math.round(minutos * 60);

  const h = Math.floor(totalSegundos / 3600);
  const m = Math.floor((totalSegundos % 3600) / 60);
  const s = totalSegundos % 60;

  if (h > 0) {
    return `${h}h ${pad(m)}min`;
  }

  if (m > 0) {
    return `${m}min ${pad(s)}s`;
  }

  return `${s}s`;
}

/*
 * Converte a data ISO (UTC) para o horário do aparelho.
 * "2026-10-08T23:59:25.039Z" -> "Hoje, 20:59" (em UTC-3)
 */
function formatarUltimaIrrigacao(iso: string | null) {
  if (!iso) {
    return 'Nenhuma ainda';
  }

  const data = new Date(iso);

  if (Number.isNaN(data.getTime())) {
    return '—';
  }

  const hora = `${pad(data.getHours())}:${pad(data.getMinutes())}`;

  const mesmoDia = (a: Date, b: Date) =>
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate();

  const hoje = new Date();
  const ontem = new Date();
  ontem.setDate(hoje.getDate() - 1);

  if (mesmoDia(data, hoje)) {
    return `Hoje, ${hora}`;
  }

  if (mesmoDia(data, ontem)) {
    return `Ontem, ${hora}`;
  }

  return `${pad(data.getDate())}/${pad(data.getMonth() + 1)}, ${hora}`;
}

/* ------------------------------------------------------------------ */
/* STATUS DOS SENSORES                                                 */
/* ------------------------------------------------------------------ */

/*
 * Retorna a situação da temperatura.
 */
function getTemperatureStatus(temperatura: number) {
  if (temperatura >= 35) {
    return 'Temperatura alta';
  }

  if (temperatura >= 20) {
    return 'Temperatura normal';
  }

  return 'Temperatura baixa';
}

/*
 * Retorna a situação da umidade do solo.
 */
function getHumidityStatus(umidade: number) {
  if (umidade < 30) {
    return 'Solo seco';
  }

  if (umidade <= 70) {
    return 'Umidade adequada';
  }

  return 'Solo muito úmido';
}