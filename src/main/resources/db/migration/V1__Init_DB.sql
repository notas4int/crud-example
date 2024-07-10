create table Protein(
    id bigint primary key generated by default as identity,
    name varchar not null,
    brand varchar not null,
    cost int check (cost > 0)
);