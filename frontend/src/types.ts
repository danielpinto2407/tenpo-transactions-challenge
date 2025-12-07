export interface Transaction {
  id: number | null;
  amount: number;
  business: string;
  tenpistaName: string;
  transactionDate: string;
}

export interface TransactionRequest {
  amount: number;
  business: string;
  tenpistaName: string;
  transactionDate: string;
}

export interface TransactionResponse extends Transaction {}
