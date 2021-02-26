## 基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github（后面 2 周的作业依然要是用到这个表结构）。

```

create table commodity
(
    id bigint auto_increment,
    commodity_id varchar(50) not null,
    name varchar(50) not null,
    price double not null,
    price_currency varchar(20) not null,
    balance int not null,
    create_time datetime(3) not null,
    create_by varchar(50) not null,
    update_time datetime(3) not null,
    update_by varchar(50) not null,
    status enum('A', 'I') not null,
    version datetime(3)  default current_timestamp (3) not null
        on update current_timestamp (3),
    constraint commodity_commodity_id_uindex
        unique (commodity_id),
    constraint commodity_id_uindex
        unique (id),
    constraint commodity_pk
        primary key (id)
);


create table buyer (
                       id bigint auto_increment,
                       
                       buyer_id varchar(50) not null,
                       name varchar(50) not null,

                       create_time datetime(3) not null,
                       create_by varchar(50) not null,
                       update_time datetime(3) not null,
                       update_by varchar(50) not null,
                       status enum('A', 'I') not null,
                       version datetime(3)  default current_timestamp (3) not null
                           on update current_timestamp (3),
                       constraint buyer_buyer_id_uindex
                           unique (buyer_id),
                       constraint buyer_id_uindex
                           unique (id),
                       constraint buyer_pk
                           primary key (id)
);

create table orders (
                       id bigint auto_increment,
                       orders_id varchar(50) not null,

                       commodity_id varchar(50) not null,
                       buyer_id varchar(50) not null,
                       commodity_quantity int not null,


                       create_time datetime(3) not null,
                       create_by varchar(50) not null,
                       update_time datetime(3) not null,
                       update_by varchar(50) not null,
                       status enum('A', 'I') not null,
                       version datetime(3)  default current_timestamp (3) not null
                           on update current_timestamp (3),
                       constraint orders_orders_id_uindex
                           unique (orders_id),
                       constraint orders_id_uindex
                           unique (id),
                       constraint orders_pk
                           primary key (id)
);


```

## INSERT 100w

```
package church.shifu.archive.berylapi;

import church.shifu.archive.berylapi.domain.membership.repository.MembershipMapper;
import church.shifu.archive.berylapi.domain.membership.service.MembershipDomSvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrdersTest {

    @Autowired
    private MembershipDomSvc membershipDomSvc;

    @Autowired
    private MembershipMapper membershipMapper;

    private void truncateOrders() {
        System.out.println("TRUNCATE `orders`!!!!");
        membershipMapper.truncateOrders();
    }

    @BeforeAll
    void beforeAll() {
        truncateOrders();
    }

    @Test
    void createOrders() {

        final long start = System.currentTimeMillis();
        membershipDomSvc.createOrders(200000);
        final long end = System.currentTimeMillis();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.printf("Total time: %d ms", end - start);
        System.out.println();
        System.out.println();
        System.out.println();
    }
}

```

### DomSvc

```
  public void createOrders(int ordersCnt){

        var rnd = new Random();

        List<Orders> ordersList = new ArrayList<>();

        for (var i = 0 ; i < ordersCnt; i++){
            ordersList.add(generateOrders(rnd));
        }

        // 9 fields
        var max = 4*1024*1024;
        var orderCntPerPartitionDbLimitation = max / 9;

        var threadCnt = 32;
        var threadLimitation = ordersCnt / threadCnt;

        var partitionLimit = Math.min(orderCntPerPartitionDbLimitation, threadLimitation);

        var listsOfOrdersList = Lists.partition(ordersList, partitionLimit);

        System.out.println("partition length: "+listsOfOrdersList.size());
        listsOfOrdersList.stream().parallel().peek((ol) -> {
            membershipRepo.addOrders(ol);
        }).collect(Collectors.toList());

    }

    private Orders generateOrders(Random rnd) {
        var ordersId = "OI-" + idSuffix();
        var commodityId = "CI-" + idSuffix();
        var buyerId = "BI-" + idSuffix();
        var commodityQuantity = rnd.nextInt(999999);

        LocalDateTime currentTime = LocalDateTime.now();
        MembershipUser creator = Membership.newMembershipUser(MembershipUserId.fromString(MOCK_CREATOR_ID));

        var order = Orders.builder()
                .ordersId(ordersId)
                .commodityId(commodityId)
                .buyerId(buyerId)
                .commodityQuantity(commodityQuantity)
                .createBy(creator)
                .createTime(currentTime)
                .updateBy(creator)
                .updateTime(currentTime)
                .status(ActivationStatus.ACTIVE)
                .build();
        return order;
    }

    @NotNull
    private String idSuffix() {
        String currentDate = BerylDateTime.currentDateTime("yyyyMMdd");
        var simpleUuid = SimpleUuid.getUuid();

        final String s = currentDate +"-"+ simpleUuid;
        return s;
    }

```


###  Repo

```
public void addOrders(List<Orders> aOrdersList){
        membershipMapper.addOrders(aOrdersList);
    }
```


### Mapper

```
    @Insert("<script> "
            + " INSERT INTO orders(orders_id, commodity_id, buyer_id, commodity_quantity, "
            + " status, create_by, create_time, update_by, update_time) "
            + " VALUES "
            + " <foreach collection='ordersList' item='orders' separator=',' >"
            + "     (#{orders.ordersId}, #{orders.commodityId}, #{orders.buyerId}, #{orders.commodityQuantity}, "
            + "         #{orders.status}, #{orders.createBy}, #{orders.createTime}, #{orders.updateBy}, "
            + "         #{orders.updateTime})"
            + " </foreach>"
            + "</script>")
    @Options(useGeneratedKeys = true)
    void addOrders(@Param("ordersList") List<Orders> ordersList);

    @Update("TRUNCATE TABLE orders")
    void truncateOrders();
```