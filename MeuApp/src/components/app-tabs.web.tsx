import {
  Tabs,
  TabList,
  TabTrigger,
  TabSlot,
  TabTriggerSlotProps,
  TabListProps,
} from 'expo-router/ui';

import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';

import {
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';

export default function AppTabs() {
  return (
    <Tabs>
      <TabSlot style={styles.tabSlot} />

      <TabList asChild>
        <CustomTabList>
          <TabTrigger name="home" href="/" asChild>
            <TabButton
              label="Home"
              icon="home-outline"
            />
          </TabTrigger>

          <TabTrigger
            name="monitoramento"
            href="/media"
            asChild
          >
            <TabButton
              label="Monitoramento"
              icon="monitor-dashboard"
            />
          </TabTrigger>

          <TabTrigger
            name="historico"
            href="/historico"
            asChild
          >
            <TabButton
              label="Histórico"
              icon="history"
            />
          </TabTrigger>
        </CustomTabList>
      </TabList>
    </Tabs>
  );
}

type TabButtonProps = TabTriggerSlotProps & {
  label: string;
  icon:
    | 'home-outline'
    | 'monitor-dashboard'
    | 'history';
};

export function TabButton({
  label,
  icon,
  isFocused,
  ...props
}: TabButtonProps) {
  return (
    <Pressable
      {...props}
      style={({ pressed }) => [
        styles.tabButton,
        pressed && styles.pressed,
      ]}
    >
      <View
        style={[
          styles.tabButtonView,
          isFocused && styles.tabButtonSelected,
        ]}
      >
        <MaterialCommunityIcons
          name={icon}
          size={24}
          color={
            isFocused
              ? '#22C55E'
              : '#94A3B8'
          }
        />

        <Text
          style={[
            styles.tabText,
            isFocused && styles.tabTextSelected,
          ]}
        >
          {label}
        </Text>
      </View>
    </Pressable>
  );
}

export function CustomTabList(
  props: TabListProps
) {
  return (
    <View
      {...props}
      style={styles.tabListContainer}
    >
      <View style={styles.innerContainer}>
        {props.children}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  tabSlot: {
    height: '100%',
  },

  tabListContainer: {
    position: 'absolute',
    bottom: 20,
    width: '100%',
    alignItems: 'center',
    paddingHorizontal: 20,
  },

  innerContainer: {
    width: '100%',
    maxWidth: 500,
    height: 70,
    backgroundColor: '#1E293B',
    borderRadius: 22,

    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',

    paddingHorizontal: 8,

    borderWidth: 1,
    borderColor: '#334155',
  },

  tabButton: {
    flex: 1,
  },

  tabButtonView: {
    height: 54,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 16,
    gap: 3,
  },

  tabButtonSelected: {
    backgroundColor: '#0F172A',
  },

  tabText: {
    color: '#94A3B8',
    fontSize: 11,
    fontWeight: '600',
  },

  tabTextSelected: {
    color: '#22C55E',
  },

  pressed: {
    opacity: 0.7,
  },
});