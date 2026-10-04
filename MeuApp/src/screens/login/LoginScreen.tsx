import { useState } from 'react';

import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  Text,
  TextInput,
  View,
} from 'react-native';

import { router } from 'expo-router';

import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';

import { requisicao } from '@/services/api';

import { styles } from './styles';

export default function LoginScreen() {
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

      const response = await requisicao(
        '/auth/login',
        'POST',
        {
          email: email.trim(),
          senha: senha,
        }
      );

      if (response.status === 200) {
        router.replace('/');
        return;
      }

      if (response.status === 409) {
        Alert.alert(
          'Erro ao entrar',
          'E-mail ou senha incorretos.'
        );

        return;
      }

      Alert.alert(
        'Erro ao entrar',
        'Não foi possível realizar o login.'
      );
    } catch (error) {
      console.log('Erro ao fazer login:', error);

      Alert.alert(
        'Erro de conexão',
        'Não foi possível conectar ao servidor.'
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