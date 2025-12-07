import { useEffect, useState } from "react";
import { getTransactionsPage } from "../api/transactionApi";
import type { Transaction, PaginatedTransactionResponse } from "../types";

interface Props {
  refreshKey: number;
}

export default function TransactionList({ refreshKey }: Props) {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [page, setPage] = useState<number>(0);
  const [size] = useState<number>(5);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchPage = async (pageToLoad: number) => {
    setLoading(true);
    try {
      const data: PaginatedTransactionResponse = await getTransactionsPage(pageToLoad, size);
      setTransactions(data.content);
      setPage(data.page);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error(error);
      alert("Error al cargar transacciones");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPage(0);
  }, [refreshKey]);

  const goTo = (p: number) => {
    if (p < 0 || (totalPages && p >= totalPages)) return;
    fetchPage(p);
  };

  return (
    <div>
      <h2>Listado de Transacciones</h2>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <>
          <ul>
            {transactions.map((t) => (
              <li key={t.id}>
                {t.tenpistaName} - {t.business} - {t.amount} - {t.transactionDate}
              </li>
            ))}
          </ul>

          <div style={{ marginTop: 12 }}>
            <button onClick={() => goTo(page - 1)} disabled={page <= 0}>
              Prev
            </button>
            <span style={{ margin: "0 8px" }}>
              Página {page + 1} de {totalPages || 1}
            </span>
            <button onClick={() => goTo(page + 1)} disabled={totalPages ? page + 1 >= totalPages : false}>
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}
