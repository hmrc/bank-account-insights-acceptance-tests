DROP TABLE IF EXISTS __schema__.bankaccount_reject;
CREATE TABLE __schema__.bankaccount_reject
(
    sort_code         TEXT      NOT NULL,
    account_number    TEXT      NOT NULL,
    requested_because TEXT      NOT NULL,
    requested_at      TIMESTAMP NOT NULL,
    requested_by      TEXT      NOT NULL,
    PRIMARY KEY (sort_code, account_number)
);

CREATE INDEX IF NOT EXISTS __schema___bankaccount_reject_sortCodeIdx on __schema__.bankaccount_reject (sort_code);
CREATE INDEX IF NOT EXISTS __schema___bankaccount_reject_accountNumberIdx on __schema__.bankaccount_reject (account_number);
