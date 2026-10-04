import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
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