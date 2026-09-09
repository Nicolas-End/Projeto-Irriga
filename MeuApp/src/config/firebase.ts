import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';

const firebaseConfig = {
  apiKey: 'AIzaSyCZwPkxzVCKHFx3eZ0551V3COfcyOHoJqY',
  authDomain: 'irrigador-inteligente-29427.firebaseapp.com',
  projectId: 'irrigador-inteligente-29427',
  storageBucket: 'irrigador-inteligente-29427.firebasestorage.app',
  messagingSenderId: '696013221863',
  appId: '1:696013221863:web:fa657c14ebe14be36a5742',
};

const app = initializeApp(firebaseConfig);

const auth = getAuth(app);

const db = getFirestore(app);

export { app, auth, db };