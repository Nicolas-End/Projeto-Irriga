import { DarkTheme, Stack, ThemeProvider } from 'expo-router';
import * as SplashScreen from 'expo-splash-screen';
import { StyleSheet, View } from 'react-native';

import { AnimatedSplashOverlay } from '@/components/animated-icon';

SplashScreen.preventAutoHideAsync();

export default function RootLayout() {
  return (
    <View style={styles.container}>
      <ThemeProvider
        value={{
          ...DarkTheme,
          colors: {
            ...DarkTheme.colors,
            background: '#0F172A',
            card: '#0F172A',
          },
        }}
      >
        <AnimatedSplashOverlay />

        <Stack
          initialRouteName="login"
          screenOptions={{
            headerShown: false,
            contentStyle: {
              backgroundColor: '#0F172A',
            },
          }}
        >
          <Stack.Screen
            name="login"
            options={{
              headerShown: false,
            }}
          />

          <Stack.Screen
            name="cadastro"
            options={{
              headerShown: false,
            }}
          />

          <Stack.Screen
            name="index"
            options={{
              headerShown: false,
            }}
          />

          <Stack.Screen
            name="media"
            options={{
              headerShown: false,
            }}
          />

          <Stack.Screen
            name="historico"
            options={{
              headerShown: false,
            }}
          />
        </Stack>
      </ThemeProvider>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#0F172A',
  },
});