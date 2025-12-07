import { useEffect, useState } from "react";
import { createTransaction } from "../api/transactionApi";
import type { TransactionRequest } from "../types";

interface Props {
  onAdded: () => void;
}

type Errors = Partial<
  Record<"amount" | "business" | "tenpistaName" | "transactionDate" | "server", string>
>;

export default function TransactionForm({ onAdded }: Props) {
  const [amount, setAmount] = useState<number>(0);
  const [loading, setLoading] = useState(false);
  const [business, setBusiness] = useState("");
  const [tenpistaName, setTenpistaName] = useState("");
  const [transactionDate, setTransactionDate] = useState("");

  const [errors, setErrors] = useState<Errors>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  const toLocalDatetimeLocal = (d: Date) => {
    const pad = (n: number) => String(n).padStart(2, "0");
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
      d.getDate()
    )}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  };

  const maxDate = toLocalDatetimeLocal(new Date());

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
        e.transactionDate = "La fecha no puede ser futura";
      }
    }

    return e;
  };

  useEffect(() => {
    setErrors(validateAll());
  }, [amount, business, tenpistaName, transactionDate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const eAll = validateAll();
    setErrors(eAll);
    setTouched({ amount: true, business: true, tenpistaName: true, transactionDate: true });

    if (Object.keys(eAll).length > 0) return;

    let payloadDate = transactionDate;
    if (/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/.test(transactionDate)) {
      payloadDate = transactionDate + ":00";
    }

    const transaction: TransactionRequest = {
      amount,
      business,
      tenpistaName,
      transactionDate: payloadDate,
    };

    try {
      setLoading(true);
      setErrors((prev) => ({ ...prev, server: undefined }));

      await createTransaction(transaction);
      onAdded();

      setAmount(0);
      setBusiness("");
      setTenpistaName("");
      setTransactionDate("");
      setTouched({});
      setErrors({});
    } catch (error: any) {
      const msg =
        error?.response?.data?.message ||
        error?.message ||
        "Error al crear transacción";
      setErrors((prev) => ({ ...prev, server: msg }));
    } finally {
      setLoading(false);
    }
  };

  const showError = (field: keyof Errors) => touched[field as string] && errors[field];

  const inputClass = (field: keyof Errors) => {
    const base =
      "w-full px-3 py-2 rounded-md transition focus:outline-none focus:ring-2 placeholder-white/70";
    const error = "border-red-500 focus:ring-red-500";
    const normal = "border-tenpoBorder focus:ring-tenpoLight/40";

    return `${base} ${showError(field) ? error : normal}`;
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="flex flex-col h-full">
      {errors.server && (
        <div className="text-red-600 mb-4 text-sm bg-red-100 border border-red-300 rounded-md p-2">
          {errors.server}
        </div>
      )}

      {/* Monto */}
      <div className="mb-4">
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
          className={inputClass("amount")}
        />
        {showError("amount") && (
          <div className="text-red-600 text-sm mt-1">{errors.amount}</div>
        )}
      </div>

      {/* Negocio */}
      <div className="mb-4">
        <input
          type="text"
          value={business}
          onChange={(e) => {
            setBusiness(e.target.value);
            setTouched((t) => ({ ...t, business: true }));
          }}
          placeholder="Negocio"
          required
          className={inputClass("business")}
        />
        {showError("business") && (
          <div className="text-red-600 text-sm mt-1">{errors.business}</div>
        )}
      </div>

      {/* Tenpista */}
      <div className="mb-4">
        <input
          type="text"
          value={tenpistaName}
          onChange={(e) => {
            setTenpistaName(e.target.value);
            setTouched((t) => ({ ...t, tenpistaName: true }));
          }}
          placeholder="Nombre Tenpista"
          required
          className={inputClass("tenpistaName")}
        />
        {showError("tenpistaName") && (
          <div className="text-red-600 text-sm mt-1">{errors.tenpistaName}</div>
        )}
      </div>

      {/* Fecha */}
      <div className="mb-4">
        <input
          type="datetime-local"
          value={transactionDate}
          onChange={(e) => {
            setTransactionDate(e.target.value);
            setTouched((t) => ({ ...t, transactionDate: true }));
          }}
          required
          max={maxDate}
          className={inputClass("transactionDate")}
        />
        {showError("transactionDate") && (
          <div className="text-red-600 text-sm mt-1">{errors.transactionDate}</div>
        )}
      </div>

      {/* Botón */}
      <button
        type="submit"
        disabled={loading || Object.keys(errors).length > 0}
        aria-disabled={loading || Object.keys(errors).length > 0}
        className={`w-full px-4 py-2 rounded-md text-primary font-semibold transition focus:outline-none focus:ring-2 focus:ring-primary/30 ${
          loading || Object.keys(errors).length > 0
            ? "bg-gray-400 cursor-not-allowed text-primary"
            : "bg-tenpoLight hover:opacity-90"
        }`}
      >
        {loading ? "Creando..." : "Agregar Transacción"}
      </button>
    </form>
  );
}
