-- REITs Platform Database Initialization Script

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table is created by JPA/Hibernate, but we can add indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_kyc_verified ON users(kyc_verified);

-- REITs table indexes
CREATE INDEX IF NOT EXISTS idx_reits_ticker ON reits(ticker);
CREATE INDEX IF NOT EXISTS idx_reits_sector ON reits(sector);
CREATE INDEX IF NOT EXISTS idx_reits_country ON reits(country);
CREATE INDEX IF NOT EXISTS idx_reits_trading_enabled ON reits(trading_enabled);

-- Portfolios table indexes
CREATE INDEX IF NOT EXISTS idx_portfolios_user_id ON portfolios(user_id);

-- Holdings table indexes
CREATE INDEX IF NOT EXISTS idx_holdings_portfolio_id ON holdings(portfolio_id);
CREATE INDEX IF NOT EXISTS idx_holdings_reit_id ON holdings(reit_id);

-- Transactions table indexes
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_reit_id ON transactions(reit_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);

-- Dividends table indexes
CREATE INDEX IF NOT EXISTS idx_dividends_reit_id ON dividends(reit_id);
CREATE INDEX IF NOT EXISTS idx_dividends_payment_date ON dividends(payment_date);
CREATE INDEX IF NOT EXISTS idx_dividends_ex_dividend_date ON dividends(ex_dividend_date);

-- Insert sample REITs data
INSERT INTO reits (ticker, name, exchange, sector, market_cap, current_price, previous_close, dividend_yield, price_to_book, occupancy_rate, country, currency, description, is_active, trading_enabled, created_at, updated_at, last_updated)
VALUES
-- Singapore REITs
('CLT', 'CapitaLand Integrated Commercial Trust', 'SGX', 'RETAIL', 10500000000, 2.15, 2.12, 5.2, 0.85, 95.5, 'Singapore', 'SGD', 'Largest REIT in Singapore with retail and office properties', true, true, NOW(), NOW(), NOW()),
('MLT', 'Mapletree Logistics Trust', 'SGX', 'INDUSTRIAL', 8200000000, 1.65, 1.63, 4.8, 1.2, 97.0, 'Singapore', 'SGD', 'Asia-focused logistics REIT', true, true, NOW(), NOW(), NOW()),
('MAGIC', 'Mapletree Industrial Trust', 'SGX', 'INDUSTRIAL', 6500000000, 2.85, 2.82, 4.5, 1.1, 96.2, 'Singapore', 'SGD', 'Diversified industrial REIT with data centers', true, true, NOW(), NOW(), NOW()),
('AEMN', 'Ascendas Real Estate Investment Trust', 'SGX', 'OFFICE', 9800000000, 2.95, 2.93, 4.3, 0.95, 94.8, 'Singapore', 'SGD', 'Business and industrial space REIT', true, true, NOW(), NOW(), NOW()),

-- US REITs
('SPG', 'Simon Property Group', 'NYSE', 'RETAIL', 42000000000, 125.50, 124.30, 5.8, 1.5, 93.2, 'United States', 'USD', 'Premier retail real estate company', true, true, NOW(), NOW(), NOW()),
('PLD', 'Prologis Inc', 'NYSE', 'INDUSTRIAL', 95000000000, 128.75, 127.90, 3.2, 2.8, 97.5, 'United States', 'USD', 'Global leader in logistics real estate', true, true, NOW(), NOW(), NOW()),
('AMT', 'American Tower Corporation', 'NYSE', 'SPECIALTY', 88000000000, 195.20, 194.50, 3.0, 4.2, 99.0, 'United States', 'USD', 'Wireless and broadcast communications infrastructure', true, true, NOW(), NOW(), NOW()),
('WELL', 'Welltower Inc', 'NYSE', 'HEALTHCARE', 48000000000, 98.45, 97.80, 3.5, 1.8, 91.5, 'United States', 'USD', 'Healthcare infrastructure REIT', true, true, NOW(), NOW(), NOW())
ON CONFLICT (ticker) DO NOTHING;

-- Insert sample dividends
INSERT INTO dividends (reit_id, amount_per_unit, ex_dividend_date, payment_date, frequency, type, currency, created_at, updated_at)
SELECT
    r.id,
    CASE r.ticker
        WHEN 'CLT' THEN 0.0275
        WHEN 'MLT' THEN 0.0198
        WHEN 'MAGIC' THEN 0.0318
        WHEN 'AEMN' THEN 0.0316
        ELSE 0.05
    END,
    CURRENT_DATE + INTERVAL '7 days',
    CURRENT_DATE + INTERVAL '30 days',
    'QUARTERLY',
    'REGULAR',
    r.currency,
    NOW(),
    NOW()
FROM reits r
WHERE r.ticker IN ('CLT', 'MLT', 'MAGIC', 'AEMN')
ON CONFLICT DO NOTHING;

-- Create admin user (password: Admin@123)
-- Note: This is a hashed password using BCrypt
INSERT INTO users (email, password, first_name, last_name, kyc_verified, account_status, created_at, updated_at)
VALUES ('admin@reitsplatform.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJ7A7vN1oF8xX.TkDN8vY5vFxJ8p8YPi', 'Admin', 'User', true, 'ACTIVE', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Add admin role
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE email = 'admin@reitsplatform.com'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER' FROM users WHERE email = 'admin@reitsplatform.com'
ON CONFLICT DO NOTHING;

-- Create admin portfolio
INSERT INTO portfolios (user_id, total_value, total_invested, unrealized_gain, realized_gain, total_dividends_received, annual_dividend_income, created_at, updated_at)
SELECT id, 0, 0, 0, 0, 0, 0, NOW(), NOW() FROM users WHERE email = 'admin@reitsplatform.com'
ON CONFLICT (user_id) DO NOTHING;
