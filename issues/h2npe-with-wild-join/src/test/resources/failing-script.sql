drop all objects;

create table Transactions ( -- t2
  id int,
  client_id int,
  state int
);

create table WFL_States ( -- t0
  id int,
  object_id int,
  state_id int
);

create table WFL_StateGroups( -- t3
  id int
);

create table WFL_States_StateGroups ( -- t4
  object_id int,
  state_id int,
  state_group_id int
);

create table TransactionsProperties ( -- t1
  transaction_id int,
  property_id int,
  value int
);

select * from {oj Transactions t2 LEFT OUTER JOIN WFL_States t0
  ON t0.state_id = t2.state AND t0.object_id = 2
--     without this subquery it works fine _and its WHERE_
    AND EXISTS (SELECT 1 FROM WFL_States_StateGroups t4
   , WFL_StateGroups t3 -- this can be omitted or switched with t4
      WHERE t4.object_id = t0.object_id -- necessary for NPE
   AND t3.id = -257  AND t4.state_id = t0.state_id AND t3.id = t4.state_group_id -- this can be omitted
    )
  }
, TransactionsProperties t1
-- if ^^^ rewritten as "(CROSS/INNER) JOIN" instead "," - it works fine, e.g.:
-- CROSS JOIN TransactionsProperties t1
  WHERE t2.client_id = 56
  AND t0.state_id IS NOT NULL
  AND t1.transaction_id = t2.id -- this throws NPE
  AND t1.property_id = 804 -- this throws NPE
  AND t1.value IS NOT NULL -- this condition works