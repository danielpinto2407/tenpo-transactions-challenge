import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TransactionForm from '../TransactionForm';
import { vi } from 'vitest';

vi.mock('../../api/transactionApi', () => ({
  createTransaction: vi.fn().mockResolvedValue({}),
}));

import { createTransaction } from '../../api/transactionApi';

describe('TransactionForm', () => {
  it('disables submit when form is invalid and calls API on valid submit', async () => {
    const onAdded = vi.fn();
    const { container } = render(<TransactionForm onAdded={onAdded} />);

    const submit = screen.getByRole('button', { name: /agregar transacción/i }) as HTMLButtonElement;

    // Initially the form is invalid so submit should be disabled
    expect(submit.disabled).toBe(true);

    const amount = screen.getByPlaceholderText(/Monto/i);
    const business = screen.getByPlaceholderText(/Negocio/i);
    const tenpista = screen.getByPlaceholderText(/Nombre Tenpista/i);
    const dateInput: HTMLInputElement | null = container.querySelector('input[type="datetime-local"]');

    // fill valid values
    await userEvent.clear(amount);
    await userEvent.type(amount, '150');
    await userEvent.type(business, 'Mi Comercio');
    await userEvent.type(tenpista, 'Pepito');

    // use a fixed past date to avoid timezone/max constraints in the test environment
    const past = '2000-01-01T00:00';
    if (dateInput) await userEvent.type(dateInput, past);

    // After filling valid values, submit should be enabled and trigger API
    await waitFor(() => expect(submit.disabled).toBe(false));

    await userEvent.click(submit);

    await waitFor(() => expect(createTransaction).toHaveBeenCalled());
    expect(onAdded).toHaveBeenCalled();
  });
});
