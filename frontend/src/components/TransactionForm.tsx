import { useState } from "react";
import { createTransaction } from "../api/transactionApi";
import type { TransactionRequest } from "../types";

interface Props {
  onAdded: () => void;
}

export default function TransactionForm({ onAdded }: Props) {
  const [amount, setAmount] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [business, setBusiness] = useState("");
  const [tenpistaName, setTenpistaName] = useState("");
  const [transactionDate, setTransactionDate] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validaciones frontend
    if (isNaN(amount)) {
      return alert("Monto inválido");
    }

    if (amount < 0) {
      return alert("El monto no puede ser negativo");
    }

    if (!business.trim() || !tenpistaName.trim() || !transactionDate) {
      return alert("Completa todos los campos");
    }

    // Validar fecha no futura
    const selectedDate = new Date(transactionDate);
    const now = new Date();
    if (selectedDate.getTime() > now.getTime()) {
      return alert("La fecha de la transacción no puede ser futura");
    }

    // Ajustar formato: datetime-local suele devolver "YYYY-MM-DDTHH:mm" (sin segundos)
    let payloadDate = transactionDate;
    if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/.test(transactionDate)) {
      payloadDate = transactionDate + ":00"; // agregar segundos
    }

    const transaction: TransactionRequest = { amount, business, tenpistaName, transactionDate: payloadDate };

    try {
      setLoading(true);
      await createTransaction(transaction);
      alert("Transacción creada");
      onAdded();
      setAmount(0);
      setBusiness("");
      setTenpistaName("");
      setTransactionDate("");
    } catch (error: any) {
      console.error(error);
      const msg = error?.response?.data?.message || error?.message || "Error al crear transacción";
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="number"
        value={Number.isNaN(amount) ? "" : amount}
        onChange={(e) => {
          const v = e.target.value;
          const parsed = v === "" ? NaN : Number(v);
          setAmount(parsed);
        }}
        placeholder="Monto"
      />
      <input type="text" value={business} onChange={(e) => setBusiness(e.target.value)} placeholder="Negocio" />
      <input type="text" value={tenpistaName} onChange={(e) => setTenpistaName(e.target.value)} placeholder="Nombre Tenpista" />
      <input type="datetime-local" value={transactionDate} onChange={(e) => setTransactionDate(e.target.value)} />
      <button type="submit" disabled={loading}>{loading ? "Creando..." : "Agregar Transacción"}</button>
    </form>
  );
}
