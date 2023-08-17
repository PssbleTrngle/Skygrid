export default function getEnv(key: string) {
  const env = process.env[key];
  if (!env) throw new Error(`Environment Variable '${key}' missing`);
  return env;
}
