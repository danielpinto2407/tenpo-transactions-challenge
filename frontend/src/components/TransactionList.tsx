import { useEffect, useState, useMemo } from "react";
import { getTransactionsPage } from "../api/transactionApi";
import type { Transaction, PaginatedTransactionResponse } from "../types";

interface Props {
  refreshKey: number;
}

const currencyFormatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',
  minimumFractionDigits: 0,
});

const dateFormatter = new Intl.DateTimeFormat('es-CL', {
  dateStyle: 'short',
  timeStyle: 'medium',
});

export default function TransactionList({ refreshKey }: Props) {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  // Iniciamos la paginación controlada por estado
  const [page, setPage] = useState<number>(0);
  const [size, setSize] = useState<number>(5);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);

  // Efecto principal de carga de datos
  useEffect(() => {
    let isActive = true; // Bandera para evitar condiciones de carrera

    const fetchData = async () => {
      setLoading(true);
      try {
        const data: PaginatedTransactionResponse = await getTransactionsPage(page, size);
        
        // Solo actualizamos el estado si el componente sigue montado y esta petición es la actual
        if (isActive) {
          setTransactions(data.content);
          setTotalPages(data.totalPages);
          // Si la API devuelve una página diferente a la solicitada (ej: out of bounds), sincronizamos
          if (data.page !== page) setPage(data.page);
        }
      } catch (error) {
        console.error(error);
        if (isActive) setTransactions([]);
      } finally {
        if (isActive) setLoading(false);
      }
    };

    fetchData();

    return () => {
      isActive = false; // Limpieza: cancela la actualización de estado si el componente se desmonta o los deps cambian
    };
  }, [page, size, refreshKey]); 
  // Nota: Al cambiar refreshKey, size o page, se dispara la carga automáticamente.

  // Handlers optimizados
  const handleSizeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSize(Number(e.target.value));
    setPage(0); // Resetear a primera página al cambiar tamaño
  };

  const goTo = (newPage: number) => {
    if (newPage >= 0 && (!totalPages || newPage < totalPages)) {
      setPage(newPage);
    }
  };

  // Optimización de rendimiento: Memoizar el array de paginación para evitar recálculos innecesarios
  const paginationButtons = useMemo(() => {
    return Array.from({ length: totalPages || 1 }, (_, i) => i);
  }, [totalPages]);

  // Clases CSS reutilizables
  const btnBaseClass = "px-3 py-1 rounded-md transition inline-flex items-center justify-center focus:outline-none focus:ring-2 focus:ring-tenpoLight/40";
  const btnActiveClass = "bg-tenpoLight text-primary border-transparent hover:opacity-90";
  const btnInactiveClass = "bg-primary text-white border-transparent";
  const btnDisabledClass = "bg-gray-400 text-primary cursor-not-allowed border-gray-200";
  

  return (
    <div>
      {/* Header y Filtros */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-2xl font-semibold">Transacciones</h2>
        <div className="flex items-center gap-3">
          <div className="text-sm text-light-600 dark:text-light-300">
            Página {page + 1} de {totalPages || 1}
          </div>
          <label className="text-sm text-gray-600 dark:text-gray-300">Mostrar</label>
          <select
            aria-label="Tamaño de página"
            value={size}
            onChange={handleSizeChange}
            className="px-2 py-1 rounded-md bg-tenpoLight text-primary border-tenpoBorder focus:ring-2 focus:ring-tenpoLight/5"
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>
      </div>

      {/* Lista de Transacciones */}
      {loading ? (
        <div className="py-10 text-center text-gray-500">Cargando transacciones...</div>
      ) : (
        <>
          {transactions.length === 0 ? (
            <div className="py-12 text-center text-gray-500">
              <p className="mb-3">No hay transacciones aún.</p>
              <p className="text-sm">Agrega la primera transacción usando el formulario.</p>
            </div>
          ) : (
            <ul className="grid gap-3">
              {transactions.map((t) => (
                <li key={t.id} className="flex items-center justify-between p-4 bg-tenpoCard border border-tenpoBorder rounded-md">
                  <div>
                    <div className="font-medium text-tenpoLight">{t.tenpistaName}</div>
                    <div className="text-sm text-gray-300">{t.business}</div>
                  </div>
                  <div className="text-right">
                    {/* Uso del formatter optimizado */}
                    <div className="text-lg font-semibold text-tenpoLight">{currencyFormatter.format(t.amount)}</div>
                    <div className="text-xs text-gray-300">
                      {dateFormatter.format(new Date(t.transactionDate))}
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          )}

          {/* Controles de Paginación */}
          <div className="mt-4 flex items-center justify-center gap-2">
            <button
              onClick={() => goTo(page - 1)}
              disabled={page <= 0}
              aria-label="Página anterior"
              className={`${btnBaseClass} ${page <= 0 ? btnDisabledClass : btnInactiveClass}`}
            >
              Prev
            </button>

            <div className="flex items-center gap-1" role="navigation" aria-label="Paginación de transacciones">
              {paginationButtons.map((i) => {
                const isActive = i === page;
                return (
                  <button
                    key={i}
                    onClick={() => goTo(i)}
                    aria-label={`Ir a la página ${i + 1}`}
                    aria-current={isActive ? 'page' : undefined}
                    className={`${btnBaseClass} ${isActive ? btnActiveClass : btnInactiveClass}`}
                  >
                    {i + 1}
                  </button>
                );
              })}
            </div>

            <button
              onClick={() => goTo(page + 1)}
              disabled={totalPages ? page + 1 >= totalPages : false}
              aria-label="Página siguiente"
              className={`${btnBaseClass} ${totalPages && page + 1 >= totalPages ? btnDisabledClass : btnInactiveClass}`}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}