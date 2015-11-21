CREATE TABLE User (
  id number auto_increment,
  name varchar2(255) default NULL,
  phone varchar2(100) default NULL,
  company varchar2(255),
  iban varchar2(34),
  PRIMARY KEY (id)
) ;

CREATE INDEX idx_id ON User (id) ;
/*
CREATE INDEX idx_name ON User (name) ;
CREATE INDEX idx_phone ON User (phone) ;
CREATE INDEX idx_company ON User (company) ;
CREATE INDEX idx_iban ON User (iban) ;
*/