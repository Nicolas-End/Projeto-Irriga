import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F172A',
  },

  scrollContent: {
    padding: 20,
    paddingTop: 55,
    paddingBottom: 40,
  },

  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 30,
  },

  emoji: {
    fontSize: 40,
    marginRight: 14,
  },

  title: {
    color: '#FFFFFF',
    fontSize: 26,
    fontWeight: 'bold',
  },

  subtitle: {
    color: '#94A3B8',
    fontSize: 14,
    marginTop: 3,
  },

  section: {
    marginBottom: 18,
  },

  sectionTitle: {
    color: '#FFFFFF',
    fontSize: 22,
    fontWeight: 'bold',
  },

  sectionDescription: {
    color: '#94A3B8',
    fontSize: 14,
    marginTop: 6,
  },

  modeCard: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 18,
    borderRadius: 16,
    backgroundColor: '#1E293B',
    borderWidth: 2,
    borderColor: 'transparent',
    marginBottom: 12,
  },

  modeCardSelected: {
    borderColor: '#22C55E',
  },

  modeIcon: {
    fontSize: 28,
    marginRight: 14,
  },

  modeContent: {
    flex: 1,
  },

  modeTitle: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: 'bold',
  },

  modeDescription: {
    color: '#94A3B8',
    fontSize: 13,
    marginTop: 4,
    lineHeight: 18,
  },

  check: {
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: '#22C55E',
    justifyContent: 'center',
    alignItems: 'center',
  },

  checkText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
    fontSize: 16,
  },

  configuration: {
    marginTop: 16,
    padding: 20,
    borderRadius: 18,
    backgroundColor: '#1E293B',
  },

  configurationTitle: {
    color: '#FFFFFF',
    fontSize: 19,
    fontWeight: 'bold',
  },

  configurationSubtitle: {
    color: '#94A3B8',
    fontSize: 13,
    marginTop: 5,
    marginBottom: 20,
  },

  inputSection: {
    marginBottom: 22,
  },

  inputTitle: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },

  inputDescription: {
    color: '#94A3B8',
    fontSize: 13,
    marginTop: 5,
    marginBottom: 10,
  },

  valueContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  valueButton: {
    width: 42,
    height: 42,
    borderRadius: 10,
    backgroundColor: '#334155',
    justifyContent: 'center',
    alignItems: 'center',
  },

  disabledButton: {
    opacity: 0.35,
  },

  valueButtonText: {
    color: '#FFFFFF',
    fontSize: 24,
    fontWeight: 'bold',
  },

  valueInput: {
    width: 70,
    height: 42,
    marginHorizontal: 10,
    borderRadius: 10,
    backgroundColor: '#0F172A',
    color: '#FFFFFF',
    textAlign: 'center',
    fontSize: 18,
    fontWeight: 'bold',
  },

  disabledInput: {
    opacity: 0.5,
  },

  unit: {
    color: '#CBD5E1',
    fontSize: 15,
    marginRight: 10,
  },

  disabledText: {
    opacity: 0.5,
  },

  lockMessage: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#0F172A',
    borderRadius: 10,
    padding: 12,
    marginTop: 2,
  },

  lockIcon: {
    fontSize: 18,
    marginRight: 8,
  },

  lockText: {
    flex: 1,
    color: '#94A3B8',
    fontSize: 12,
    lineHeight: 17,
  },

  saveButton: {
    height: 54,
    marginTop: 20,
    borderRadius: 14,
    backgroundColor: '#22C55E',
    justifyContent: 'center',
    alignItems: 'center',
  },

  saveButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },
});