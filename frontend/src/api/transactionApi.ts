import axios from "axios";
import type { TransactionRequest, TransactionResponse } from "../types";

const BASE = (import.meta as any).env?.VITE_API_BASE_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: BASE,
  headers: { "Content-Type": "application/json" },
});

export const getTransactions = async (): Promise<TransactionResponse[]> => {
  const res = await api.get<TransactionResponse[]>("/transactions");
  return res.data;
};

export const createTransaction = async (transaction: TransactionRequest): Promise<TransactionResponse> => {
  const res = await api.post<TransactionResponse>("/transactions", transaction);
  return res.data;
};
