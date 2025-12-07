import { useState } from "react";
import TransactionForm from "../components/TransactionForm";
import TransactionList from "../components/TransactionList";

export default function Dashboard() {
  const [refresh, setRefresh] = useState(0);

  return (
    <div>
      <h1>Panel de Cliente</h1>
      <TransactionForm onAdded={() => setRefresh((prev) => prev + 1)} />
      <TransactionList refreshKey={refresh} />
    </div>
  );
}
