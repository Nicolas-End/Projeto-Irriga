
const API_URL = process.env.EXPO_PUBLIC_API_URL;

type Metodo = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

export async function requisicao<T>(
  endpoint: string,
  metodo: Metodo = 'GET',
  body?: unknown
): Promise<{ status: number; data: T | null }> {

  const response = await fetch(
    `${API_URL}${endpoint}`,
    {
      method: metodo,

      headers: {
        'Content-Type': 'application/json',
      },

      body: body
        ? JSON.stringify(body)
        : undefined,
    }
  );

  let data: T | null = null;

  try {
    data = await response.json();
  } catch {
    // Resposta sem JSON
  }

  return {
    status: response.status,
    data,
  };
}