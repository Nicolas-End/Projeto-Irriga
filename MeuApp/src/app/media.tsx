import { useEffect, useState } from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

export default function Media() {
  // Dados simulados dos sensores
  const [temperatura, setTemperatura] = useState(28.6);
  const [umidade, setUmidade] = useState(47);

  /*
   * SIMULAÇÃO DO SENSOR
   *
   * Por enquanto os valores mudam a cada 3 segundos.
   *
   * FUTURAMENTE:
   * essa parte será substituída pelos dados recebidos
   * do Arduino.
   */
  useEffect(() => {
    const interval = setInterval(() => {
      // Simula pequenas alterações na temperatura
      setTemperatura((valorAtual) => {
        const variacao = Math.random() * 0.6 - 0.3;
        return Number((valorAtual + variacao).toFixed(1));
      });

      // Simula pequenas alterações na umidade
      setUmidade((valorAtual) => {
        const variacao = Math.floor(Math.random() * 3) - 1;

        const novoValor = valorAtual + variacao;

        // Impede valores menores que 0 ou maiores que 100
        return Math.min(100, Math.max(0, novoValor));
      });
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      {/* Cabeçalho */}
      <View style={styles.header}>
        <Text style={styles.title}>
          Monitoramento
        </Text>

        <Text style={styles.subtitle}>
          Acompanhe os dados do sistema de irrigação
        </Text>
      </View>

      {/* STATUS */}
      <View style={styles.status}>
        <View style={styles.statusDot} />

        <Text style={styles.statusText}>
          Dados em tempo real
        </Text>
      </View>

      {/* SENSORES */}
      <Text style={styles.sectionTitle}>
        Sensores
      </Text>

      <View style={styles.sensorContainer}>
        {/* Temperatura */}
        <View style={styles.sensorCard}>
          <Text style={styles.sensorIcon}>
            🌡️
          </Text>

          <Text style={styles.sensorTitle}>
            Temperatura
          </Text>

          <View style={styles.sensorValueContainer}>
            <Text style={styles.sensorValue}>
              {temperatura.toFixed(1)}
            </Text>

            <Text style={styles.sensorUnit}>
              °C
            </Text>
          </View>

          <Text style={styles.sensorStatus}>
            {getTemperatureStatus(temperatura)}
          </Text>
        </View>

        {/* Umidade */}
        <View style={styles.sensorCard}>
          <Text style={styles.sensorIcon}>
            🌱
          </Text>

          <Text style={styles.sensorTitle}>
            Umidade do solo
          </Text>

          <View style={styles.sensorValueContainer}>
            <Text style={styles.sensorValue}>
              {umidade}
            </Text>

            <Text style={styles.sensorUnit}>
              %
            </Text>
          </View>

          <Text style={styles.sensorStatus}>
            {getHumidityStatus(umidade)}
          </Text>
        </View>
      </View>

      {/* CONSUMO */}
      <Text style={styles.sectionTitle}>
        Consumo
      </Text>

      {/* Água */}
      <View style={styles.card}>
        <View style={styles.cardHeader}>
          <Text style={styles.icon}>
            💧
          </Text>

          <View>
            <Text style={styles.cardTitle}>
              Consumo de água
            </Text>

            <Text style={styles.cardSubtitle}>
              Este mês
            </Text>
          </View>
        </View>

        <Text style={styles.value}>
          1.250 L
        </Text>
      </View>

      {/* Energia */}
      <View style={styles.card}>
        <View style={styles.cardHeader}>
          <Text style={styles.icon}>
            ⚡
          </Text>

          <View>
            <Text style={styles.cardTitle}>
              Consumo de energia
            </Text>

            <Text style={styles.cardSubtitle}>
              Este mês
            </Text>
          </View>
        </View>

        <Text style={styles.value}>
          18,4 kWh
        </Text>
      </View>

      {/* Tempo ligado */}
      <View style={styles.card}>
        <View style={styles.cardHeader}>
          <Text style={styles.icon}>
            ⏱️
          </Text>

          <View>
            <Text style={styles.cardTitle}>
              Tempo ligado
            </Text>

            <Text style={styles.cardSubtitle}>
              Tempo total de funcionamento
            </Text>
          </View>
        </View>

        <Text style={styles.value}>
          42h 35min
        </Text>
      </View>

      {/* VIDA ÚTIL */}
      <View style={styles.lifeCard}>
        <Text style={styles.lifeTitle}>
          ⚙️ Vida útil do sistema
        </Text>

        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>
            Tempo de funcionamento
          </Text>

          <Text style={styles.infoValue}>
            42h 35min
          </Text>
        </View>

        <View style={styles.separator} />

        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>
            Irrigações realizadas
          </Text>

          <Text style={styles.infoValue}>
            127
          </Text>
        </View>

        <View style={styles.separator} />

        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>
            Última irrigação
          </Text>

          <Text style={styles.infoValue}>
            Hoje, 18:32
          </Text>
        </View>
      </View>
    </ScrollView>
  );
}

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

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F172A',
  },

  content: {
    padding: 20,
    paddingTop: 55,
    paddingBottom: 40,
  },

  header: {
    marginBottom: 18,
  },

  title: {
    color: '#FFFFFF',
    fontSize: 28,
    fontWeight: 'bold',
  },

  subtitle: {
    color: '#94A3B8',
    fontSize: 14,
    marginTop: 5,
  },

  status: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 26,
  },

  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: '#22C55E',
    marginRight: 8,
  },

  statusText: {
    color: '#22C55E',
    fontSize: 13,
    fontWeight: '600',
  },

  sectionTitle: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 14,
  },

  /*
   * SENSORES
   */

  sensorContainer: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 30,
  },

  sensorCard: {
    flex: 1,
    backgroundColor: '#1E293B',
    borderRadius: 18,
    padding: 16,
    minHeight: 180,
  },

  sensorIcon: {
    fontSize: 27,
    marginBottom: 12,
  },

  sensorTitle: {
    color: '#94A3B8',
    fontSize: 13,
    fontWeight: '600',
  },

  sensorValueContainer: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    marginTop: 10,
  },

  sensorValue: {
    color: '#FFFFFF',
    fontSize: 30,
    fontWeight: 'bold',
  },

  sensorUnit: {
    color: '#94A3B8',
    fontSize: 15,
    marginLeft: 4,
    marginBottom: 4,
  },

  sensorStatus: {
    color: '#22C55E',
    fontSize: 11,
    fontWeight: '600',
    marginTop: 10,
  },

  /*
   * CONSUMO
   */

  card: {
    backgroundColor: '#1E293B',
    borderRadius: 18,
    padding: 20,
    marginBottom: 14,
  },

  cardHeader: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  icon: {
    fontSize: 30,
    marginRight: 14,
  },

  cardTitle: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },

  cardSubtitle: {
    color: '#94A3B8',
    fontSize: 12,
    marginTop: 3,
  },

  value: {
    color: '#FFFFFF',
    fontSize: 30,
    fontWeight: 'bold',
    marginTop: 18,
  },

  /*
   * VIDA ÚTIL
   */

  lifeCard: {
    backgroundColor: '#1E293B',
    borderRadius: 18,
    padding: 20,
    marginTop: 5,
  },

  lifeTitle: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 20,
  },

  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
  },

  infoLabel: {
    color: '#94A3B8',
    fontSize: 13,
    flex: 1,
  },

  infoValue: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: 'bold',
  },

  separator: {
    height: 1,
    backgroundColor: '#334155',
    marginVertical: 5,
  },
});