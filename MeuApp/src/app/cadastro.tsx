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

import { createUserWithEmailAndPassword } from 'firebase/auth';

import { auth } from '@/config/firebase';

export default function Cadastro() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');

  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [carregando, setCarregando] = useState(false);

  async function criarConta() {
    if (!email.trim() || !senha || !confirmarSenha) {
      Alert.alert(
        'Atenção',
        'Preencha todos os campos.'
      );
      return;
    }

    if (senha !== confirmarSenha) {
      Alert.alert(
        'Atenção',
        'As senhas não são iguais.'
      );
      return;
    }

    if (senha.length < 6) {
      Alert.alert(
        'Atenção',
        'A senha precisa ter pelo menos 6 caracteres.'
      );
      return;
    }

    try {
      setCarregando(true);

      await createUserWithEmailAndPassword(
        auth,
        email.trim(),
        senha
      );

      Alert.alert(
        'Conta criada',
        'Seu cadastro foi realizado com sucesso.',
        [
          {
            text: 'Continuar',
            onPress: () => router.replace('/'),
          },
        ]
      );
    } catch (error: any) {
      console.log('Erro no cadastro:', error);

      let mensagem =
        'Não foi possível criar sua conta.';

      switch (error.code) {
        case 'auth/email-already-in-use':
          mensagem =
            'Este e-mail já possui uma conta.';
          break;

        case 'auth/invalid-email':
          mensagem =
            'O e-mail informado é inválido.';
          break;

        case 'auth/weak-password':
          mensagem =
            'A senha informada é muito fraca.';
          break;

        case 'auth/network-request-failed':
          mensagem =
            'Não foi possível conectar ao Firebase.';
          break;
      }

      Alert.alert(
        'Erro no cadastro',
        mensagem
      );
    } finally {
      setCarregando(false);
    }
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
        <View style={styles.logoContainer}>
          <MaterialCommunityIcons
            name="account-plus-outline"
            size={42}
            color="#22C55E"
          />
        </View>

        <Text style={styles.title}>
          Criar conta
        </Text>

        <Text style={styles.subtitle}>
          Cadastre-se para acessar o Projeto Irriga.
        </Text>

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
          />
        </View>

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
          />

          <Pressable
            onPress={() =>
              setMostrarSenha(
                (valor) => !valor
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

        <Text style={styles.label}>
          Confirmar senha
        </Text>

        <View style={styles.inputContainer}>
          <MaterialCommunityIcons
            name="lock-check-outline"
            size={21}
            color="#94A3B8"
          />

          <TextInput
            style={styles.input}
            value={confirmarSenha}
            onChangeText={setConfirmarSenha}
            placeholder="Digite sua senha novamente"
            placeholderTextColor="#64748B"
            secureTextEntry={!mostrarSenha}
            autoCapitalize="none"
          />
        </View>

        <Pressable
          disabled={carregando}
          style={[
            styles.button,
            carregando && styles.buttonDisabled,
          ]}
          onPress={criarConta}
        >
          {carregando ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <>
              <Text style={styles.buttonText}>
                Criar conta
              </Text>

              <MaterialCommunityIcons
                name="arrow-right"
                size={20}
                color="#FFFFFF"
              />
            </>
          )}
        </Pressable>

        <View style={styles.loginContainer}>
          <Text style={styles.loginText}>
            Já possui uma conta?
          </Text>

          <Pressable
            onPress={() =>
              router.replace('./login')
            }
          >
            <Text style={styles.loginButton}>
              Entrar
            </Text>
          </Pressable>
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
    width: 80,
    height: 80,
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
    marginTop: 8,
    marginBottom: 30,
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

  button: {
    height: 54,
    backgroundColor: '#22C55E',
    borderRadius: 14,
    marginTop: 28,
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row',
    gap: 8,
  },

  buttonDisabled: {
    opacity: 0.6,
  },

  buttonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: 'bold',
  },

  loginContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 24,
    gap: 5,
  },

  loginText: {
    color: '#94A3B8',
    fontSize: 13,
  },

  loginButton: {
    color: '#22C55E',
    fontSize: 13,
    fontWeight: 'bold',
  },
});