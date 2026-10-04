import {
  ScrollView,
  Text,
  View,
} from 'react-native';

import { styles } from './styles';

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

export default function HistoricoScreen() {
  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      showsVerticalScrollIndicator={false}
    >
      <View style={styles.header}>
        <Text style={styles.title}>
          Histórico
        </Text>

        <Text style={styles.subtitle}>
          Acompanhe as irrigações realizadas pelo sistema
        </Text>
      </View>

      <View style={styles.summary}>
        <View>
          <Text style={styles.summaryLabel}>
            Irrigações registradas
          </Text>

          <Text style={styles.summaryValue}>
            {historico.length}
          </Text>
        </View>

        <Text style={styles.summaryIcon}>
          💧
        </Text>
      </View>

      <Text style={styles.sectionTitle}>
        Atividades recentes
      </Text>

      {historico.map((item) => (
        <View
          key={item.id}
          style={styles.card}
        >
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

          <InfoRow
            icon="🌡️"
            label="Temperatura"
            value={`${item.temperatura.toFixed(1)} °C`}
          />

          <InfoRow
            icon="💧"
            label="Umidade do solo"
            value={`${item.umidade}%`}
          />

          <InfoRow
            icon="⏱️"
            label="Tempo ligado"
            value={`${item.tempoLigado} min`}
          />

          <InfoRow
            icon="💦"
            label="Água utilizada"
            value={`${item.aguaUtilizada.toFixed(1)} L`}
          />

          <InfoRow
            icon="⚙️"
            label="Modo"
            value={item.modo}
          />

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
        <Text style={styles.infoIcon}>
          {icon}
        </Text>

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