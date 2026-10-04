import React, { useState } from 'react';

import {
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

type IrrigationMode =
  | 'economica'
  | 'equilibrada'
  | 'customizada';

const MODE_CONFIG = {
  economica: {
    temperature: '32',
    humidity: '30',
    irrigationTime: '5',
  },

  equilibrada: {
    temperature: '30',
    humidity: '40',
    irrigationTime: '10',
  },
};

export default function HomeScreen() {
  const [mode, setMode] =
    useState<IrrigationMode>('equilibrada');

  const [temperature, setTemperature] =
    useState(
      MODE_CONFIG.equilibrada.temperature
    );

  const [humidity, setHumidity] =
    useState(
      MODE_CONFIG.equilibrada.humidity
    );

  const [irrigationTime, setIrrigationTime] =
    useState(
      MODE_CONFIG.equilibrada.irrigationTime
    );

  const selectMode = (
    selectedMode: IrrigationMode
  ) => {
    setMode(selectedMode);

    if (selectedMode === 'economica') {
      setTemperature(
        MODE_CONFIG.economica.temperature
      );

      setHumidity(
        MODE_CONFIG.economica.humidity
      );

      setIrrigationTime(
        MODE_CONFIG.economica.irrigationTime
      );
    }

    if (selectedMode === 'equilibrada') {
      setTemperature(
        MODE_CONFIG.equilibrada.temperature
      );

      setHumidity(
        MODE_CONFIG.equilibrada.humidity
      );

      setIrrigationTime(
        MODE_CONFIG.equilibrada.irrigationTime
      );
    }
  };

  const changeValue = (
    value: string,
    setValue: React.Dispatch<
      React.SetStateAction<string>
    >,
    amount: number
  ) => {
    if (mode !== 'customizada') {
      return;
    }

    const number = Number(value) || 0;

    setValue(
      String(
        Math.max(0, number + amount)
      )
    );
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
        `Temperatura: ${temperature} °C\n` +
        `Umidade: ${humidity}%\n` +
        `Tempo de irrigação: ${irrigationTime} minutos`
    );
  };

  const isEditable =
    mode === 'customizada';

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={
        Platform.OS === 'ios'
          ? 'padding'
          : undefined
      }
    >
      <ScrollView
        contentContainerStyle={
          styles.scrollContent
        }
        showsVerticalScrollIndicator={false}
      >

        {/* CABEÇALHO */}

        <View style={styles.header}>
          <Text style={styles.emoji}>
            🌱
          </Text>

          <View>
            <Text style={styles.title}>
              Projeto Irriga
            </Text>

            <Text style={styles.subtitle}>
              Configuração de irrigação
            </Text>
          </View>
        </View>

        {/* TÍTULO */}

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>
            Definição de irrigação
          </Text>

          <Text
            style={styles.sectionDescription}
          >
            Escolha como o sistema deverá
            realizar a irrigação.
          </Text>
        </View>

        {/* ECONÔMICA */}

        <Pressable
          style={[
            styles.modeCard,
            mode === 'economica' &&
              styles.modeCardSelected,
          ]}
          onPress={() =>
            selectMode('economica')
          }
        >
          <Text style={styles.modeIcon}>
            💧
          </Text>

          <View style={styles.modeContent}>
            <Text style={styles.modeTitle}>
              Econômica
            </Text>

            <Text
              style={styles.modeDescription}
            >
              Prioriza a economia de água,
              utilizando parâmetros definidos
              pelo sistema.
            </Text>
          </View>

          {mode === 'economica' && (
            <View style={styles.check}>
              <Text style={styles.checkText}>
                ✓
              </Text>
            </View>
          )}
        </Pressable>

        {/* EQUILIBRADA */}

        <Pressable
          style={[
            styles.modeCard,
            mode === 'equilibrada' &&
              styles.modeCardSelected,
          ]}
          onPress={() =>
            selectMode('equilibrada')
          }
        >
          <Text style={styles.modeIcon}>
            ⚖️
          </Text>

          <View style={styles.modeContent}>
            <Text style={styles.modeTitle}>
              Equilibrada
            </Text>

            <Text
              style={styles.modeDescription}
            >
              Equilibra o consumo de água e
              a necessidade da plantação.
            </Text>
          </View>

          {mode === 'equilibrada' && (
            <View style={styles.check}>
              <Text style={styles.checkText}>
                ✓
              </Text>
            </View>
          )}
        </Pressable>

        {/* CUSTOMIZADA */}

        <Pressable
          style={[
            styles.modeCard,
            mode === 'customizada' &&
              styles.modeCardSelected,
          ]}
          onPress={() =>
            selectMode('customizada')
          }
        >
          <Text style={styles.modeIcon}>
            ⚙️
          </Text>

          <View style={styles.modeContent}>
            <Text style={styles.modeTitle}>
              Customizada
            </Text>

            <Text
              style={styles.modeDescription}
            >
              Permite configurar manualmente
              os parâmetros da irrigação.
            </Text>
          </View>

          {mode === 'customizada' && (
            <View style={styles.check}>
              <Text style={styles.checkText}>
                ✓
              </Text>
            </View>
          )}
        </Pressable>

        {/* CONFIGURAÇÕES */}

        <View style={styles.configuration}>
          <Text
            style={styles.configurationTitle}
          >
            Parâmetros da irrigação
          </Text>

          <Text
            style={styles.configurationSubtitle}
          >
            Modo selecionado: {getModeName()}
          </Text>

          {/* TEMPERATURA */}

          <View style={styles.inputSection}>
            <Text style={styles.inputTitle}>
              🌡️ Temperatura
            </Text>

            <Text
              style={styles.inputDescription}
            >
              Irrigar quando a temperatura
              estiver acima de:
            </Text>

            <View style={styles.valueContainer}>
              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    temperature,
                    setTemperature,
                    -1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  −
                </Text>
              </Pressable>

              <TextInput
                style={[
                  styles.valueInput,
                  !isEditable &&
                    styles.disabledInput,
                ]}
                value={temperature}
                onChangeText={setTemperature}
                keyboardType="numeric"
                editable={isEditable}
                maxLength={3}
              />

              <Text
                style={[
                  styles.unit,
                  !isEditable &&
                    styles.disabledText,
                ]}
              >
                °C
              </Text>

              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    temperature,
                    setTemperature,
                    1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  +
                </Text>
              </Pressable>
            </View>
          </View>

          {/* UMIDADE */}

          <View style={styles.inputSection}>
            <Text style={styles.inputTitle}>
              💧 Umidade do solo
            </Text>

            <Text
              style={styles.inputDescription}
            >
              Irrigar quando a umidade estiver
              abaixo de:
            </Text>

            <View style={styles.valueContainer}>
              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    humidity,
                    setHumidity,
                    -1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  −
                </Text>
              </Pressable>

              <TextInput
                style={[
                  styles.valueInput,
                  !isEditable &&
                    styles.disabledInput,
                ]}
                value={humidity}
                onChangeText={setHumidity}
                keyboardType="numeric"
                editable={isEditable}
                maxLength={3}
              />

              <Text
                style={[
                  styles.unit,
                  !isEditable &&
                    styles.disabledText,
                ]}
              >
                %
              </Text>

              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    humidity,
                    setHumidity,
                    1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  +
                </Text>
              </Pressable>
            </View>
          </View>

          {/* TEMPO */}

          <View style={styles.inputSection}>
            <Text style={styles.inputTitle}>
              ⏱️ Tempo de irrigação
            </Text>

            <Text
              style={styles.inputDescription}
            >
              Durante quanto tempo o sistema
              irá irrigar:
            </Text>

            <View style={styles.valueContainer}>
              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    irrigationTime,
                    setIrrigationTime,
                    -1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  −
                </Text>
              </Pressable>

              <TextInput
                style={[
                  styles.valueInput,
                  !isEditable &&
                    styles.disabledInput,
                ]}
                value={irrigationTime}
                onChangeText={
                  setIrrigationTime
                }
                keyboardType="numeric"
                editable={isEditable}
                maxLength={3}
              />

              <Text
                style={[
                  styles.unit,
                  !isEditable &&
                    styles.disabledText,
                ]}
              >
                min
              </Text>

              <Pressable
                disabled={!isEditable}
                style={[
                  styles.valueButton,
                  !isEditable &&
                    styles.disabledButton,
                ]}
                onPress={() =>
                  changeValue(
                    irrigationTime,
                    setIrrigationTime,
                    1
                  )
                }
              >
                <Text
                  style={
                    styles.valueButtonText
                  }
                >
                  +
                </Text>
              </Pressable>
            </View>
          </View>

          {/* AVISO */}

          {!isEditable && (
            <View style={styles.lockMessage}>
              <Text style={styles.lockIcon}>
                🔒
              </Text>

              <Text style={styles.lockText}>
                Os parâmetros deste modo são
                definidos automaticamente pelo
                sistema.
              </Text>
            </View>
          )}
        </View>

        {/* SALVAR */}

        <Pressable
          style={styles.saveButton}
          onPress={saveConfiguration}
        >
          <Text style={styles.saveButtonText}>
            Salvar configuração
          </Text>
        </Pressable>

      </ScrollView>
    </KeyboardAvoidingView>
  );
}