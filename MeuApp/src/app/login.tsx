
import { useState } from 'react';

import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';

import { router } from 'expo-router';

import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';

import { signInWithEmailAndPassword } from 'firebase/auth';

import { auth } from '@/config/firebase';

export default function Login() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');

  const [mostrarSenha, setMostrarSenha] = useState(false);

  const [carregando, setCarregando] = useState(false);

  async function fazerLogin() {
    if (!email.trim() || !senha.trim()) {
      Alert.alert(
        'Atenção',
        'Preencha o e-mail e a senha.'
      );

      return;
    }

    try {
      setCarregando(true);

      await signInWithEmailAndPassword(
        auth,
        email.trim(),
        senha
      );

      router.replace('/');
    } catch (error: any) {
      console.log('Erro no login:', error);

      let mensagem =
        'Não foi possível realizar o login.';

      switch (error.code) {
        case 'auth/invalid-email':
          mensagem =
            'O e-mail informado é inválido.';
          break;

        case 'auth/invalid-credential':
          mensagem =
            'E-mail ou senha incorretos.';
          break;

        case 'auth/user-disabled':
          mensagem =
            'Este usuário foi desativado.';
          break;

        case 'auth/too-many-requests':
          mensagem =
            'Muitas tentativas de login. Tente novamente mais tarde.';
          break;

        case 'auth/network-request-failed':
          mensagem =
            'Não foi possível conectar ao Firebase. Verifique sua internet.';
          break;
      }

      Alert.alert(
        'Erro ao entrar',
        mensagem
      );
    } finally {
      setCarregando(false);
    }
  }

  function abrirCadastro() {
    router.push('/cadastro');
  }

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={
        Platform.OS === 'ios'
          ? 'padding'
          : undefined
      }
    >
      <View style={styles.content}>

        {/* LOGO */}

        <View style={styles.logoContainer}>
          <MaterialCommunityIcons
            name="sprout"
            size={45}
            color="#22C55E"
          />
        </View>

        {/* TÍTULO */}

        <Text style={styles.title}>
          Projeto Irriga
        </Text>

        <Text style={styles.subtitle}>
          Acesse sua conta para gerenciar seu
          sistema de irrigação.
        </Text>

        {/* FORMULÁRIO */}

        <View style={styles.form}>

          {/* E-MAIL */}

          <Text style={styles.label}>
            E-mail
          </Text>

          <View style={styles.inputContainer}>

            <MaterialCommunityIcons
              name="email-outline"
              size={21}
              color="#94A3B8"
            />

            <TextInput
              style={styles.input}
              value={email}
              onChangeText={setEmail}
              placeholder="seuemail@email.com"
              placeholderTextColor="#64748B"
              keyboardType="email-address"
              autoCapitalize="none"
              autoCorrect={false}
              editable={!carregando}
            />

          </View>

          {/* SENHA */}

          <Text style={styles.label}>
            Senha
          </Text>

          <View style={styles.inputContainer}>

            <MaterialCommunityIcons
              name="lock-outline"
              size={21}
              color="#94A3B8"
            />

            <TextInput
              style={styles.input}
              value={senha}
              onChangeText={setSenha}
              placeholder="Digite sua senha"
              placeholderTextColor="#64748B"
              secureTextEntry={!mostrarSenha}
              autoCapitalize="none"
              editable={!carregando}
              onSubmitEditing={fazerLogin}
            />

            <Pressable
              disabled={carregando}
              onPress={() =>
                setMostrarSenha(
                  valor => !valor
                )
              }
            >
              <MaterialCommunityIcons
                name={
                  mostrarSenha
                    ? 'eye-off-outline'
                    : 'eye-outline'
                }
                size={22}
                color="#94A3B8"
              />
            </Pressable>

          </View>

          {/* ESQUECI A SENHA */}

          <Pressable
            style={styles.forgotButton}
            disabled={carregando}
          >
            <Text style={styles.forgotText}>
              Esqueci minha senha
            </Text>
          </Pressable>

          {/* BOTÃO ENTRAR */}

          <Pressable
            disabled={carregando}
            style={({ pressed }) => [
              styles.loginButton,

              pressed &&
                !carregando &&
                styles.buttonPressed,

              carregando &&
                styles.buttonDisabled,
            ]}
            onPress={fazerLogin}
          >

            {carregando ? (
              <ActivityIndicator
                color="#FFFFFF"
              />
            ) : (
              <>
                <Text style={styles.loginButtonText}>
                  Entrar
                </Text>

                <MaterialCommunityIcons
                  name="arrow-right"
                  size={20}
                  color="#FFFFFF"
                />
              </>
            )}

          </Pressable>

          {/* CRIAR CONTA */}

          <View style={styles.registerContainer}>

            <Text style={styles.registerText}>
              Ainda não possui uma conta?
            </Text>

            <Pressable
              disabled={carregando}
              onPress={abrirCadastro}
              style={({ pressed }) => [
                styles.registerButtonContainer,
                pressed &&
                  styles.registerButtonPressed,
              ]}
            >

              <Text style={styles.registerButton}>
                Criar conta
              </Text>

            </Pressable>

          </View>

        </View>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({

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
