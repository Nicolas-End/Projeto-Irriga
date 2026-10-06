import React, { useCallback, useEffect, useState } from 'react';

import {
  ActivityIndicator,
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  Text,
  TextInput,
  View,
} from 'react-native';

import { styles } from './styles';
// ajuste o caminho conforme onde está o seu arquivo da função `requisicao`
import { requisicao } from '@/services/api'

/* ------------------------------------------------------------------ */
/* TIPOS                                                               */
/* ------------------------------------------------------------------ */

type IrrigationMode = 'economica' | 'equilibrada' | 'customizada';

type InfoIrrigaResponse = {
  datas: {
    arduino: string;
    configuacao: string; // (sic) mesmo nome que vem da API
    duracaoIrrigacao: number;
    intervaloIrrigacao: number;
    umidadeMinima: number;
    usuarioEmail: string;
  };
  message: string;
  status: string;
  sucess: boolean; // (sic)
};

/* ------------------------------------------------------------------ */
/* PRESETS DOS MODOS FIXOS                                             */
/* ------------------------------------------------------------------ */
/* ATENÇÃO: os valores de `interval` são suposições,                   */
/* ajuste para os valores reais do seu sistema.                        */

const MODE_CONFIG = {
  economica: {
    humidity: '30',
    irrigationTime: '5',
    interval: '60',
  },
  equilibrada: {
    humidity: '40',
    irrigationTime: '10',
    interval: '30',
  },
};

/* ------------------------------------------------------------------ */
/* HELPERS                                                             */
/* ------------------------------------------------------------------ */

// "CUSTOMIZADA" | "Econômica" | "equilibrada" -> IrrigationMode
const parseMode = (value: string): IrrigationMode => {
  const normalized = (value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim();

  if (normalized === 'economica') return 'economica';
  if (normalized === 'equilibrada') return 'equilibrada';
  return 'customizada';
};

/* ------------------------------------------------------------------ */
/* COMPONENTE REUTILIZÁVEL PARA CADA PARÂMETRO                         */
/* ------------------------------------------------------------------ */

type ParameterInputProps = {
  title: string;
  description: string;
  value: string;
  unit: string;
  editable: boolean;
  onChange: (value: string) => void;
  onStep: (amount: number) => void;
};

function ParameterInput({
  title,
  description,
  value,
  unit,
  editable,
  onChange,
  onStep,
}: ParameterInputProps) {
  return (
    <View style={styles.inputSection}>
      <Text style={styles.inputTitle}>{title}</Text>

      <Text style={styles.inputDescription}>{description}</Text>

      <View style={styles.valueContainer}>
        <Pressable
          disabled={!editable}
          style={[styles.valueButton, !editable && styles.disabledButton]}
          onPress={() => onStep(-1)}
        >
          <Text style={styles.valueButtonText}>−</Text>
        </Pressable>

        <TextInput
          style={[styles.valueInput, !editable && styles.disabledInput]}
          value={value}
          onChangeText={onChange}
          keyboardType="numeric"
          editable={editable}
          maxLength={3}
        />

        <Text style={[styles.unit, !editable && styles.disabledText]}>
          {unit}
        </Text>

        <Pressable
          disabled={!editable}
          style={[styles.valueButton, !editable && styles.disabledButton]}
          onPress={() => onStep(1)}
        >
          <Text style={styles.valueButtonText}>+</Text>
        </Pressable>
      </View>
    </View>
  );
}

/* ------------------------------------------------------------------ */
/* TELA                                                                */
/* ------------------------------------------------------------------ */

export default function HomeScreen() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [arduino, setArduino] = useState('');
  const [userEmail, setUserEmail] = useState('');

  const [mode, setMode] = useState<IrrigationMode>('equilibrada');
  const [humidity, setHumidity] = useState(MODE_CONFIG.equilibrada.humidity);
  const [irrigationTime, setIrrigationTime] = useState(
    MODE_CONFIG.equilibrada.irrigationTime
  );
  const [interval, setInterval] = useState(MODE_CONFIG.equilibrada.interval);

  /* ---------------------- GET /info-irriga ---------------------- */

  const loadInfo = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const { status, data } = await requisicao<InfoIrrigaResponse>(
        '/info-irriga',
        'GET'
      );

      if (status !== 200 || !data?.sucess || !data.datas) {
        setError(data?.message ?? 'Não foi possível carregar as informações.');
        return;
      }

      const info = data.datas;

      setArduino(info.arduino);
      setUserEmail(info.usuarioEmail);
      setMode(parseMode(info.configuacao));

      // Os valores exibidos sempre vêm do servidor,
      // independente do modo (fixo ou customizado).
      setHumidity(String(info.umidadeMinima));
      setIrrigationTime(String(info.duracaoIrrigacao));
      setInterval(String(info.intervaloIrrigacao));
    } catch {
      setError('Falha de conexão com o servidor.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadInfo();
  }, [loadInfo]);

  /* ------------------------- Interações ------------------------- */

  const selectMode = (selectedMode: IrrigationMode) => {
    setMode(selectedMode);

    if (selectedMode === 'customizada') {
      // mantém os valores atuais para o usuário editar
      return;
    }

    const preset = MODE_CONFIG[selectedMode];
    setHumidity(preset.humidity);
    setIrrigationTime(preset.irrigationTime);
    setInterval(preset.interval);
  };

  const isEditable = mode === 'customizada';

  const changeValue = (
    value: string,
    setValue: React.Dispatch<React.SetStateAction<string>>,
    amount: number
  ) => {
    if (!isEditable) return;

    const number = Number(value) || 0;
    setValue(String(Math.max(0, number + amount)));
  };

  const getModeName = () => {
    switch (mode) {
      case 'economica':
        return 'Econômica';
      case 'equilibrada':
        return 'Equilibrada';
      case 'customizada':
        return 'Customizada';
    }
  };

  const saveConfiguration = () => {
    Alert.alert(
      'Configuração salva',
      `Modo: ${getModeName()}\n\n` +
        `Umidade mínima: ${humidity}%\n` +
        `Intervalo entre irrigações: ${interval} min\n` +
        `Duração da irrigação: ${irrigationTime} min`
    );
  };

  /* --------------------------- Render --------------------------- */

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
      >
        {/* CABEÇALHO (sempre visível) */}

        <View style={styles.header}>
          <Text style={styles.emoji}>🌱</Text>

          <View>
            <Text style={styles.title}>Projeto Irriga</Text>
            <Text style={styles.subtitle}>Configuração de irrigação</Text>
          </View>
        </View>

        {/* CARREGANDO */}

        {loading && (
          <View style={styles.configuration}>
            <ActivityIndicator size="large" color="#22C55E" />

            <Text
              style={[
                styles.configurationSubtitle,
                { textAlign: 'center', marginTop: 14, marginBottom: 0 },
              ]}
            >
              Carregando configuração...
            </Text>
          </View>
        )}

        {/* ERRO */}

        {!loading && error && (
          <View style={styles.configuration}>
            <Text style={styles.configurationTitle}>⚠️ Ops, algo deu errado</Text>

            <Text style={styles.configurationSubtitle}>{error}</Text>

            <Pressable style={styles.saveButton} onPress={loadInfo}>
              <Text style={styles.saveButtonText}>Tentar novamente</Text>
            </Pressable>
          </View>
        )}

        {/* CONTEÚDO */}

        {!loading && !error && (
          <>
            {/* DISPOSITIVO / USUÁRIO */}

            <View style={styles.modeCard}>
              <Text style={styles.modeIcon}>🔌</Text>

              <View style={styles.modeContent}>
                <Text style={styles.modeTitle}>Arduino {arduino}</Text>
                <Text style={styles.modeDescription}>{userEmail}</Text>
              </View>
            </View>

            {/* TÍTULO */}

            <View style={[styles.section, { marginTop: 12 }]}>
              <Text style={styles.sectionTitle}>Definição de irrigação</Text>

              <Text style={styles.sectionDescription}>
                Escolha como o sistema deverá realizar a irrigação.
              </Text>
            </View>

            {/* ECONÔMICA */}

            <Pressable
              style={[
                styles.modeCard,
                mode === 'economica' && styles.modeCardSelected,
              ]}
              onPress={() => selectMode('economica')}
            >
              <Text style={styles.modeIcon}>💧</Text>

              <View style={styles.modeContent}>
                <Text style={styles.modeTitle}>Econômica</Text>
                <Text style={styles.modeDescription}>
                  Prioriza a economia de água, utilizando parâmetros definidos
                  pelo sistema.
                </Text>
              </View>

              {mode === 'economica' && (
                <View style={styles.check}>
                  <Text style={styles.checkText}>✓</Text>
                </View>
              )}
            </Pressable>

            {/* EQUILIBRADA */}

            <Pressable
              style={[
                styles.modeCard,
                mode === 'equilibrada' && styles.modeCardSelected,
              ]}
              onPress={() => selectMode('equilibrada')}
            >
              <Text style={styles.modeIcon}>⚖️</Text>

              <View style={styles.modeContent}>
                <Text style={styles.modeTitle}>Equilibrada</Text>
                <Text style={styles.modeDescription}>
                  Equilibra o consumo de água e a necessidade da plantação.
                </Text>
              </View>

              {mode === 'equilibrada' && (
                <View style={styles.check}>
                  <Text style={styles.checkText}>✓</Text>
                </View>
              )}
            </Pressable>

            {/* CUSTOMIZADA */}

            <Pressable
              style={[
                styles.modeCard,
                mode === 'customizada' && styles.modeCardSelected,
              ]}
              onPress={() => selectMode('customizada')}
            >
              <Text style={styles.modeIcon}>⚙️</Text>

              <View style={styles.modeContent}>
                <Text style={styles.modeTitle}>Customizada</Text>
                <Text style={styles.modeDescription}>
                  Permite configurar manualmente os parâmetros da irrigação.
                </Text>
              </View>

              {mode === 'customizada' && (
                <View style={styles.check}>
                  <Text style={styles.checkText}>✓</Text>
                </View>
              )}
            </Pressable>

            {/* CONFIGURAÇÕES */}

            <View style={styles.configuration}>
              <Text style={styles.configurationTitle}>
                Parâmetros da irrigação
              </Text>

              <Text style={styles.configurationSubtitle}>
                Modo selecionado: {getModeName()}
              </Text>

              <ParameterInput
                title="💧 Umidade do solo"
                description="Irrigar quando a umidade estiver abaixo de:"
                value={humidity}
                unit="%"
                editable={isEditable}
                onChange={setHumidity}
                onStep={(amount) => changeValue(humidity, setHumidity, amount)}
              />

              <ParameterInput
                title="🔁 Intervalo entre irrigações"
                description="Tempo mínimo de espera entre uma irrigação e outra:"
                value={interval}
                unit="min"
                editable={isEditable}
                onChange={setInterval}
                onStep={(amount) => changeValue(interval, setInterval, amount)}
              />

              <ParameterInput
                title="⏱️ Duração da irrigação"
                description="Durante quanto tempo o sistema irá irrigar:"
                value={irrigationTime}
                unit="min"
                editable={isEditable}
                onChange={setIrrigationTime}
                onStep={(amount) =>
                  changeValue(irrigationTime, setIrrigationTime, amount)
                }
              />

              {/* AVISO */}

              {!isEditable && (
                <View style={styles.lockMessage}>
                  <Text style={styles.lockIcon}>🔒</Text>

                  <Text style={styles.lockText}>
                    Os parâmetros deste modo são definidos automaticamente pelo
                    sistema.
                  </Text>
                </View>
              )}
            </View>

            {/* SALVAR */}

            <Pressable style={styles.saveButton} onPress={saveConfiguration}>
              <Text style={styles.saveButtonText}>Salvar configuração</Text>
            </Pressable>
          </>
        )}
      </ScrollView>
    </KeyboardAvoidingView>
  );
}