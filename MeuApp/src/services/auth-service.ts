import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  signOut,
} from 'firebase/auth';

import { auth } from '@/config/firebase';

export function cadastrar(email: string, senha: string) {
  return createUserWithEmailAndPassword(auth, email, senha);
}

export function entrar(email: string, senha: string) {
  return signInWithEmailAndPassword(auth, email, senha);
}

export function sair() {
  return signOut(auth);
}