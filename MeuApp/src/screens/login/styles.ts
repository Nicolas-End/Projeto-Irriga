import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F172A',
  },

  content: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 24,
  },

  logoContainer: {
    width: 82,
    height: 82,

    borderRadius: 24,

    backgroundColor: '#162D29',

    justifyContent: 'center',
    alignItems: 'center',

    alignSelf: 'center',

    marginBottom: 20,
  },

  title: {
    color: '#FFFFFF',

    fontSize: 30,
    fontWeight: 'bold',

    textAlign: 'center',
  },

  subtitle: {
    color: '#94A3B8',

    fontSize: 14,

    textAlign: 'center',

    lineHeight: 20,

    marginTop: 8,
    marginBottom: 34,
  },

  form: {
    width: '100%',
  },

  label: {
    color: '#CBD5E1',

    fontSize: 14,
    fontWeight: '600',

    marginBottom: 8,
    marginTop: 14,
  },

  inputContainer: {
    height: 54,

    backgroundColor: '#1E293B',

    borderRadius: 14,

    borderWidth: 1,
    borderColor: '#334155',

    paddingHorizontal: 15,

    flexDirection: 'row',
    alignItems: 'center',
  },

  input: {
    flex: 1,

    marginLeft: 10,

    color: '#FFFFFF',

    fontSize: 15,
  },

  forgotButton: {
    alignSelf: 'flex-end',

    marginTop: 12,
  },

  forgotText: {
    color: '#22C55E',

    fontSize: 13,
    fontWeight: '600',
  },

  loginButton: {
    height: 54,

    backgroundColor: '#22C55E',

    borderRadius: 14,

    marginTop: 28,

    justifyContent: 'center',
    alignItems: 'center',

    flexDirection: 'row',

    gap: 8,
  },

  loginButtonText: {
    color: '#FFFFFF',

    fontSize: 16,
    fontWeight: 'bold',
  },

  buttonPressed: {
    opacity: 0.8,
  },

  buttonDisabled: {
    opacity: 0.6,
  },

  registerContainer: {
    alignItems: 'center',

    justifyContent: 'center',

    marginTop: 26,

    paddingBottom: 10,
  },

  registerText: {
    color: '#94A3B8',

    fontSize: 14,

    marginBottom: 5,
  },

  registerButtonContainer: {
    paddingHorizontal: 14,
    paddingVertical: 6,
  },

  registerButtonPressed: {
    opacity: 0.6,
  },

  registerButton: {
    color: '#22C55E',

    fontSize: 15,

    fontWeight: 'bold',
  },
});