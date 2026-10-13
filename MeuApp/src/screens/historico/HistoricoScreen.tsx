import { useCallback, useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  Pressable,
  RefreshControl,
  ScrollView,
  Text,
  View,
} from 'react-native';

import { styles } from './styles';
import { buscarHistorico, HistoricoItem } from "@/screens/historico/apiService";

export default function HistoricoScreen() {
  const [historico, setHistorico] = useState<HistoricoItem[] | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [atualizando, setAtualizando] = useState(false);
  const [erro, setErro] = useState<string | null>(null);

  /* ---------------- GET /consumo-agua (via service) ---------------- */

  const carregar = useCallback(async (puxouParaAtualizar = false) => {
    if (puxouParaAtualizar) {
      setAtualizando(true);
    } else {
      setCarregando(true);
    }

    setErro(null);

    const resultado = await buscarHistorico();

    if (!resultado.sucesso || !resultado.dados) {
      if (puxouParaAtualizar) {
        // mantém a lista atual na tela e só avisa do problema
        Alert.alert('Não foi possível atualizar', resultado.mensagem);
      } else {
        setErro(resultado.mensagem);
      }
    } else {
      setHistorico(resultado.dados);
    }

    setCarregando(false);
    setAtualizando(false);
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  /* --------------------------- Render --------------------------- */

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
      refreshControl={
        <RefreshControl
          refreshing={atualizando}
          onRefresh={() => carregar(true)}
          tintColor="#22C55E"
          colors={['#22C55E']}
        />
      }
    >
      {/* CABEÇALHO (sempre visível) */}

      <View style={styles.header}>
        <Text style={styles.title}>Histórico</Text>

        <Text style={styles.subtitle}>
          Acompanhe as irrigações realizadas pelo sistema
        </Text>
      </View>

      {/* CARREGANDO */}

      {carregando && !historico && (
        <View style={styles.card}>
          <ActivityIndicator size="large" color="#22C55E" />

          <Text style={[styles.date, { textAlign: 'center', marginTop: 14 }]}>
            Carregando histórico...
          </Text>
        </View>
      )}

      {/* ERRO */}

      {!carregando && !historico && erro && (
        <View style={styles.card}>
          <Text style={styles.cardTitle}>⚠️ Ops, algo deu errado</Text>

          <Text style={styles.date}>{erro}</Text>

          <Pressable style={styles.saveButton} onPress={() => carregar()}>
            <Text style={styles.saveButtonText}>
              Tentar novamente
            </Text>
          </Pressable>
        </View>
      )}

      {/* CONTEÚDO */}

      {historico && (
        <>
          <View style={styles.summary}>
            <View>
              <Text style={styles.summaryLabel}>Irrigações registradas</Text>

              <Text style={styles.summaryValue}>{historico.length}</Text>
            </View>

            <Text style={styles.summaryIcon}>💧</Text>
          </View>

          <Text style={styles.sectionTitle}>Atividades recentes</Text>

          {/* LISTA VAZIA */}

          {historico.length === 0 && (
            <View style={styles.card}>
              <Text style={styles.cardTitle}>Nenhuma irrigação ainda</Text>

              <Text style={styles.date}>
                Quando o sistema irrigar, os registros aparecerão aqui.
              </Text>
            </View>
          )}

          {/* LISTA */}

          {historico.map((item, index) => (
            <View key={index} style={styles.card}>
              <View style={styles.cardHeader}>
                <View style={styles.statusContainer}>
                  <View style={styles.statusDot} />

                  <View>
                    <Text style={styles.cardTitle}>Irrigação realizada</Text>

                    <Text style={styles.date}>{formatarData(item.data)}</Text>
                  </View>
                </View>
              </View>

              <View style={styles.separator} />

              <InfoRow
                icon="💧"
                label="Umidade do solo"
                value={`${formatarNumero(item.umidade)}%`}
              />

              <InfoRow
                icon="⏱️"
                label="Tempo ligado"
                value={`${formatarNumero(item.tempoLigadoMinutos)} min`}
              />

              <InfoRow
                icon="💦"
                label="Água utilizada"
                value={`${formatarNumero(item.aguaLitros)} L`}
              />

              <InfoRow
                icon="⚙️"
                label="Modo"
                value={item.modo ?? 'Não informado'}
              />

              <InfoRow
                icon="🔌"
                label="Dispositivo"
                value={`Arduino ${item.dispositivo}`}
              />
            </View>
          ))}
        </>
      )}
    </ScrollView>
  );
}

/* ------------------------------------------------------------------ */
/* AUXILIARES                                                          */
/* ------------------------------------------------------------------ */

const pad = (n: number) => String(n).padStart(2, '0');

/*
 * Converte a data ISO (UTC) para o horário do aparelho.
 * "2026-10-10T22:29:37.989Z" -> "10/10/2026 • 19:29" (em UTC-3)
 */
function formatarData(iso: string | null) {
  if (!iso) {
    return 'Data não informada';
  }

  const data = new Date(iso);

  if (Number.isNaN(data.getTime())) {
    return 'Data não informada';
  }

  const dia = `${pad(data.getDate())}/${pad(data.getMonth() + 1)}/${data.getFullYear()}`;
  const hora = `${pad(data.getHours())}:${pad(data.getMinutes())}`;

  return `${dia} • ${hora}`;
}

/*
 * 4.0 -> "4" | 3.7 -> "3,7"
 */
function formatarNumero(valor: number) {
  return String(Number(valor.toFixed(1))).replace('.', ',');
}

function InfoRow({
  icon,
  label,
  value,
}: {
  icon: string;
  label: string;
  value: string;
}) {
  return (
    <View style={styles.infoRow}>
      <View style={styles.infoLeft}>
        <Text style={styles.infoIcon}>{icon}</Text>

        <Text style={styles.infoLabel}>{label}</Text>
      </View>

      <Text style={styles.infoValue}>{value}</Text>
    </View>
  );
}