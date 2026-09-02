import { ScrollView, StyleSheet, Text, View } from 'react-native';

type HistoricoItem = {
  id: number;
  data: string;
  horario: string;
  temperatura: number;
  umidade: number;
  tempoLigado: number;
  aguaUtilizada: number;
  modo: 'Econômica' | 'Equilibrada' | 'Customizada';
  dispositivo: string;
};

const historico: HistoricoItem[] = [
  {
    id: 1,
    data: '01/09/2026',
    horario: '18:42',
    temperatura: 31.4,
    umidade: 37,
    tempoLigado: 8,
    aguaUtilizada: 4.2,
    modo: 'Equilibrada',
    dispositivo: 'Arduino UNO',
  },
  {
    id: 2,
    data: '01/09/2026',
    horario: '12:15',
    temperatura: 33.1,
    umidade: 28,
    tempoLigado: 5,
    aguaUtilizada: 2.8,
    modo: 'Econômica',
    dispositivo: 'Arduino UNO',
  },
  {
    id: 3,
    data: '31/08/2026',
    horario: '17:30',
    temperatura: 29.8,
    umidade: 35,
    tempoLigado: 10,
    aguaUtilizada: 5.1,
    modo: 'Customizada',
    dispositivo: 'Arduino UNO',
  },
];

export default function Historico() {
  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      {/* Cabeçalho */}
      <View style={styles.header}>
        <Text style={styles.title}>Histórico</Text>

        <Text style={styles.subtitle}>
          Acompanhe as irrigações realizadas pelo sistema
        </Text>
      </View>

      {/* Quantidade */}
      <View style={styles.summary}>
        <View>
          <Text style={styles.summaryLabel}>
            Irrigações registradas
          </Text>

          <Text style={styles.summaryValue}>
            {historico.length}
          </Text>
        </View>

        <Text style={styles.summaryIcon}>💧</Text>
      </View>

      {/* Histórico */}
      <Text style={styles.sectionTitle}>
        Atividades recentes
      </Text>

      {historico.map((item) => (
        <View key={item.id} style={styles.card}>
          {/* Cabeçalho do card */}
          <View style={styles.cardHeader}>
            <View style={styles.statusContainer}>
              <View style={styles.statusDot} />

              <View>
                <Text style={styles.cardTitle}>
                  Irrigação realizada
                </Text>

                <Text style={styles.date}>
                  {item.data} • {item.horario}
                </Text>
              </View>
            </View>
          </View>

          <View style={styles.separator} />

          {/* Temperatura */}
          <InfoRow
            icon="🌡️"
            label="Temperatura"
            value={`${item.temperatura.toFixed(1)} °C`}
          />

          {/* Umidade */}
          <InfoRow
            icon="💧"
            label="Umidade do solo"
            value={`${item.umidade}%`}
          />

          {/* Tempo */}
          <InfoRow
            icon="⏱️"
            label="Tempo ligado"
            value={`${item.tempoLigado} min`}
          />

          {/* Água */}
          <InfoRow
            icon="💦"
            label="Água utilizada"
            value={`${item.aguaUtilizada.toFixed(1)} L`}
          />

          {/* Modo */}
          <InfoRow
            icon="⚙️"
            label="Modo"
            value={item.modo}
          />

          {/* Arduino */}
          <InfoRow
            icon="🔌"
            label="Dispositivo"
            value={item.dispositivo}
          />
        </View>
      ))}
    </ScrollView>
  );
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

        <Text style={styles.infoLabel}>
          {label}
        </Text>
      </View>

      <Text style={styles.infoValue}>
        {value}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F172A',
  },

  content: {
    padding: 20,
    paddingTop: 55,
    paddingBottom: 50,
  },

  header: {
    marginBottom: 24,
  },

  title: {
    color: '#FFFFFF',
    fontSize: 28,
    fontWeight: 'bold',
  },

  subtitle: {
    color: '#94A3B8',
    fontSize: 14,
    marginTop: 6,
    lineHeight: 20,
  },

  summary: {
    backgroundColor: '#1E293B',
    borderRadius: 18,
    padding: 20,
    marginBottom: 28,

    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  summaryLabel: {
    color: '#94A3B8',
    fontSize: 13,
  },

  summaryValue: {
    color: '#FFFFFF',
    fontSize: 30,
    fontWeight: 'bold',
    marginTop: 5,
  },

  summaryIcon: {
    fontSize: 36,
  },

  sectionTitle: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 14,
  },

  card: {
    backgroundColor: '#1E293B',
    borderRadius: 18,
    padding: 18,
    marginBottom: 14,
  },

  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  statusDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#22C55E',
    marginRight: 12,
  },

  cardTitle: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: 'bold',
  },

  date: {
    color: '#94A3B8',
    fontSize: 12,
    marginTop: 3,
  },

  separator: {
    height: 1,
    backgroundColor: '#334155',
    marginVertical: 16,
  },

  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    minHeight: 38,
  },

  infoLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },

  infoIcon: {
    fontSize: 16,
    width: 28,
  },

  infoLabel: {
    color: '#94A3B8',
    fontSize: 13,
  },

  infoValue: {
    color: '#FFFFFF',
    fontSize: 13,
    fontWeight: '600',
    textAlign: 'right',
  },
});