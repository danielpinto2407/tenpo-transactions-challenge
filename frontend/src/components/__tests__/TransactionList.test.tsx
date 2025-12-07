import { render, screen } from '@testing-library/react';
import TransactionList from '../TransactionList';
import { vi } from 'vitest';

// use function declaration so it's hoisted and available to the vi.mock factory
function mockPage() {
  return {
    content: [
      { id: '1', amount: 100, business: 'Comercio A', tenpistaName: 'Juan', transactionDate: new Date().toISOString() },
    ],
    page: 0,
    size: 5,
    totalElements: 1,
    totalPages: 1,
  };
}

vi.mock('../../api/transactionApi', () => ({
  getTransactionsPage: vi.fn().mockResolvedValue(mockPage()),
}));

describe('TransactionList', () => {
  it('renders transactions and pagination controls', async () => {
    render(<TransactionList refreshKey={0} />);

    expect(await screen.findByText(/Comercio A/)).toBeTruthy();
    // Pagination buttons use aria-labels in the implementation
    expect(screen.getByRole('button', { name: /Página anterior/i })).toBeTruthy();
    expect(screen.getByRole('button', { name: /Página siguiente/i })).toBeTruthy();
  });
});
