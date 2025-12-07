import { useEffect, useState } from "react";
import { createTransaction } from "../api/transactionApi";
import type { TransactionRequest } from "../types";

interface Props {
  onAdded: () => void;
}

type Errors = Partial<Record<"amount" | "business" | "tenpistaName" | "transactionDate" | "server", string>>;

export default function TransactionForm({ onAdded }: Props) {
  const [amount, setAmount] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [business, setBusiness] = useState("");
  const [tenpistaName, setTenpistaName] = useState("");
  const [transactionDate, setTransactionDate] = useState("");

  const [errors, setErrors] = useState<Errors>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  const validateAll = (): Errors => {
    const e: Errors = {};

    if (Number.isNaN(amount)) {
      e.amount = "Monto inválido";
    } else if (amount < 0) {
      e.amount = "El monto no puede ser negativo";
    }

    if (!business.trim()) {
      e.business = "Completa el nombre del negocio";
    }

    if (!tenpistaName.trim()) {
      e.tenpistaName = "Completa el nombre del tenpista";
    }

    if (!transactionDate) {
      e.transactionDate = "Selecciona la fecha y hora";
    } else {
      const selectedDate = new Date(transactionDate);
      const now = new Date();
      if (selectedDate.getTime() > now.getTime()) {
        e.transactionDate = "La fecha de la transacción no puede ser futura";
      }
    }

    return e;
  };

  useEffect(() => {
    // real-time validation: update errors when values change
    const e = validateAll();
    setErrors(e);
  }, [amount, business, tenpistaName, transactionDate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const eAll = validateAll();
    setErrors(eAll);
    setTouched({ amount: true, business: true, tenpistaName: true, transactionDate: true });

    if (Object.keys(eAll).length > 0) {
      return; // prevent submit when there are validation errors
    }

    // Ajustar formato: datetime-local suele devolver "YYYY-MM-DDTHH:mm" (sin segundos)
    let payloadDate = transactionDate;
    if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/.test(transactionDate)) {
      payloadDate = transactionDate + ":00"; // agregar segundos
    }

    const transaction: TransactionRequest = { amount, business, tenpistaName, transactionDate: payloadDate };

    try {
      setLoading(true);
      setErrors((prev) => ({ ...prev, server: undefined }));
      await createTransaction(transaction);
      onAdded();
      // reset form
      setAmount(0);
      setBusiness("");
      setTenpistaName("");
      setTransactionDate("");
      setTouched({});
      setErrors({});
    } catch (error: any) {
      console.error(error);
      const msg = error?.response?.data?.message || error?.message || "Error al crear transacción";
      setErrors((prev) => ({ ...prev, server: msg }));
    } finally {
      setLoading(false);
    }
  };

  const showError = (field: keyof Errors) => {
    return touched[field as string] && errors[field];
  };

  return (
    <form onSubmit={handleSubmit} noValidate>
      {errors.server && <div style={{ color: "#a00", marginBottom: 8 }}>{errors.server}</div>}

      <div style={{ marginBottom: 8 }}>
        <input
          type="number"
          value={Number.isNaN(amount) ? "" : amount}
          onChange={(e) => {
            const v = e.target.value;
            const parsed = v === "" ? NaN : Number(v);
            setAmount(parsed);
            setTouched((t) => ({ ...t, amount: true }));
          }}
          placeholder="Monto"
          required
          min={0}
          step={1}
          style={{ width: "100%" }}
        />
        {showError("amount") && <div style={{ color: "#a00" }}>{errors.amount}</div>}
      </div>

      <div style={{ marginBottom: 8 }}>
        <input
          type="text"
          value={business}
          onChange={(e) => {
            setBusiness(e.target.value);
            setTouched((t) => ({ ...t, business: true }));
          }}
          placeholder="Negocio"
          required
          style={{ width: "100%" }}
        />
        {showError("business") && <div style={{ color: "#a00" }}>{errors.business}</div>}
      </div>

      <div style={{ marginBottom: 8 }}>
        <input
          type="text"
          value={tenpistaName}
          onChange={(e) => {
            setTenpistaName(e.target.value);
            setTouched((t) => ({ ...t, tenpistaName: true }));
          }}
          placeholder="Nombre Tenpista"
          required
          style={{ width: "100%" }}
        />
        {showError("tenpistaName") && <div style={{ color: "#a00" }}>{errors.tenpistaName}</div>}
      </div>

      <div style={{ marginBottom: 8 }}>
        <input
          type="datetime-local"
          value={transactionDate}
          onChange={(e) => {
            setTransactionDate(e.target.value);
            setTouched((t) => ({ ...t, transactionDate: true }));
          }}
          required
          style={{ width: "100%" }}
        />
        {showError("transactionDate") && <div style={{ color: "#a00" }}>{errors.transactionDate}</div>}
      </div>

      <button type="submit" disabled={loading || Object.keys(errors).length > 0}>
        {loading ? "Creando..." : "Agregar Transacción"}
      </button>
    </form>
  );
}
