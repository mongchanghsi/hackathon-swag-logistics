DROP TABLE IF EXISTS participant;

CREATE TABLE participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(250) NOT NULL,
    contact_number VARCHAR(250) NOT NULL,
    team_name VARCHAR(250) NOT NULL,
    tshirt_size VARCHAR(250) NOT NULL
);

INSERT INTO participant (name, email, contact_number, team_name, tshirt_size) VALUES
    ('John Doe', 'johndoe@example.com', '12345','TheJohns', 'S'),
    ('John Dough', 'johndough@example.com', '12345', 'TheJohns', 'S'),
    ('John Doh', 'johndoh@example.com', '12345', 'TheJohns', 'S');
