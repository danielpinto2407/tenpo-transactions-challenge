import { useState } from "react";
import TransactionForm from "../components/TransactionForm";
import TransactionList from "../components/TransactionList";

export default function Dashboard() {
  const [refresh, setRefresh] = useState(0);

  return (
    <div className="h-screen w-screen bg-primary text-white flex flex-col">
      
      {/* Header */}
      <header className="p-6 pb-2">
        <h1 className="text-3xl font-bold text-tenpoLight">
          Tenpo - Transacciones
        </h1>
        <p className="text-sm opacity-80">
          Registro de transacciones y visualización paginada.
        </p>
      </header>

      {/* Contenido principal */}
      <main className="flex-1 grid grid-cols-1 md:grid-cols-3 gap-6 p-6 overflow-hidden">

        {/* Columna izquierda */}
        <section className="bg-tenpoCard text-gray-200 border border-tenpoBorder p-4 rounded-lg shadow-sm flex flex-col overflow-hidden">
          <h2 className="text-xl font-semibold mb-3 text-tenpoLight">
            Registro de transacciones
          </h2>
          <div className="flex-1 overflow-y-auto">
            <TransactionForm onAdded={() => setRefresh(prev => prev + 1)} />
          </div>
        </section>

        {/* Columna derecha */}
        <section className="md:col-span-2 bg-tenpoCard text-gray-200 border border-tenpoBorder p-4 rounded-lg shadow-sm flex flex-col overflow-hidden">
          <h2 className="text-xl font-semibold mb-3 text-tenpoLight">
            Transacciones
          </h2>
          <div className="flex-1 overflow-y-auto">
            <TransactionList refreshKey={refresh} />
          </div>
        </section>

      </main>
    </div>
  );
}
