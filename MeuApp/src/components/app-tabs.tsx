import { NativeTabs } from 'expo-router/unstable-native-tabs';

export default function AppTabs() {
  return (
    <NativeTabs
      backgroundColor="#0F172A"
      tintColor="#22C55E"
      iconColor={{
        default: '#94A3B8',
        selected: '#22C55E',
      }}
      labelStyle={{
        default: {
          color: '#94A3B8',
        },
        selected: {
          color: '#22C55E',
        },
      }}
    >
      <NativeTabs.Trigger name="index">
        <NativeTabs.Trigger.Icon
          md={{
            default: 'home',
            selected: 'home',
          }}
          sf={{
            default: 'house',
            selected: 'house.fill',
          }}
        />

        <NativeTabs.Trigger.Label>
          Home
        </NativeTabs.Trigger.Label>
      </NativeTabs.Trigger>

      <NativeTabs.Trigger name="media">
        <NativeTabs.Trigger.Icon
          md={{
            default: 'monitoring',
            selected: 'monitoring',
          }}
          sf={{
            default: 'chart.bar',
            selected: 'chart.bar.fill',
          }}
        />

        <NativeTabs.Trigger.Label>
          Monitoramento
        </NativeTabs.Trigger.Label>
      </NativeTabs.Trigger>

      <NativeTabs.Trigger name="historico">
        <NativeTabs.Trigger.Icon
          md={{
            default: 'history',
            selected: 'history',
          }}
          sf={{
            default: 'clock.arrow.circlepath',
            selected: 'clock.arrow.circlepath',
          }}
        />

        <NativeTabs.Trigger.Label>
          Histórico
        </NativeTabs.Trigger.Label>
      </NativeTabs.Trigger>
    </NativeTabs>
  );
}