-- Add audit columns to transactions
ALTER TABLE transactions ADD COLUMN created_at timestamptz;
ALTER TABLE transactions ADD COLUMN updated_at timestamptz;

-- Backfill created_at using transaction_date when present
UPDATE transactions
SET created_at = transaction_date AT TIME ZONE 'UTC'
WHERE transaction_date IS NOT NULL AND created_at IS NULL;

-- For remaining rows, set created_at to now()
UPDATE transactions
SET created_at = NOW()
WHERE created_at IS NULL;

-- Initialize updated_at
UPDATE transactions
SET updated_at = created_at
WHERE updated_at IS NULL;

-- Add index to speed up ordering by created_at
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions (created_at DESC);
