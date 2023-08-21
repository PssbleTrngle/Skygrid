import { useMemo, useReducer } from "react";

function loadSavedValue(key: string) {
  if (typeof window === "undefined") return false;

  const raw = localStorage.getItem(key);
  if (raw) return raw === "true";
  return null;
}

function useLocalPreference(key: string, defaultValue: boolean) {
  const storageKey = useMemo(() => `skygrid.preferences.${key}`, [key]);

  const initial = useMemo(() => loadSavedValue(storageKey), [storageKey]);

  return useReducer((_: boolean, value: boolean) => {
    localStorage.setItem(storageKey, `${value}`);
    return value;
  }, initial ?? defaultValue);
}

export function useAnimationPreference() {
  return useLocalPreference("animations", true);
}
