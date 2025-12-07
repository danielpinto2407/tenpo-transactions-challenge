import { useEffect, useState } from "react";
import { getTransactions } from "../api/transactionApi";
import type { Transaction } from "../types";

interface Props {
  refreshKey: number;
}

export default function TransactionList({ refreshKey }: Props) {
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  const fetchTransactions = async () => {
    try {
      const data = await getTransactions();
      setTransactions(data);
    } catch (error) {
      console.error(error);
      alert("Error al cargar transacciones");
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, [refreshKey]);

  return (
    <div>
      <h2>Listado de Transacciones</h2>
      <ul>
        {transactions.map((t) => (
          <li key={t.id}>
            {t.tenpistaName} - {t.business} - {t.amount} - {t.transactionDate}
          </li>
        ))}
      </ul>
    </div>
  );
}
