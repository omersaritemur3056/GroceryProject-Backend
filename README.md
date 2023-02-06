# GroceryProject-Backend

Projenin amacı nedir, neye çözüm sağlar?


Orta ölçekli bir market için tasarlanmış bu web uygulaması, marketin hem kendi içerisindeki dinamiği hem de uygulamanın e-ticaret özelliği sayesinde sistemin daha iyi 
bir şekilde işlemesi ve sektörde pazarlama ile teknolojiyi buluşturup kararlı bir büyüme sağlayıp alanında öncü olmasını hedeflemektedir.

Stok takibi, ürünlerin yönetimi, market çalışanlarının durumlarını kontrol altına alarak marketin kendi içerisindeki dinamiğini düzenlemek ve geliştirmesi en temel  amaçlarından birisidir.

İkincisi ve en az birincisi kadar önemli önemli çözümü, müşterilerine online olarak alışveriş imkanı sağlaması ve siparişlerini konforlarını bozmadan kapılarına kadar teslim edilmesi sayesinde zaman kazancı vaat etmesi.


Projemizde hangi teknolojilerden yararlandık?


Java diliyle geliştirdiğimiz bu uygulamamızda Spring Boot 3.0.2 sürümüyle REST APİ'mizi 
tasarladık ve JDK olarak versiyon 17'yi kullandık.
Veritabanı olarak PostgreSql'i tercih ettik. 
ORM(Object Relation Mapping) olarak JPA(Jakarta Persistence Api)'dan yararlandık.
Güvenlik kısmında JWT(Json Web Token) ile kullanıcı bilgilerini daha efektif işledik.
Lombok teknolojisinin sağladığı avantajlardan yararlandık.
Objeler arasındaki mapleme işlemini gerçekleştirmek amacıyla mapper olarak ModelMapper'a karar kıldık.
Geliştirme sırasında konsola ve veri olarak takip edebilmek için kullandığımız loglama
teknolojisi olarak SLF4J(Simple Logging Facade for Java)'i tercih ettik.
SMPT(Simple Mail Transfer Protocol) protokolü ile mail servisini oluşturduk.
Geliştirme sırasında pratikliğinden dolayı Swagger(Open UI)'dan yararlandık.
Resim yükleme için bulut teknolojisi kullanmak istedik ve bunu Cloudinary servisiyle gerçekleştirdik.
Market çalışanlarının Türk vatandaşı olup olmadığını bulabilmek için Nüfus Müdürlüğünün 
API'sini(Mernis doğrulaması) kullandık ve bunu easyWSDL aracıyla API'imize getirdik.


Projede ne gibi uygulamalar yapıldığını merak ederseniz projeyi inceleyebilir, sorunuz olur ya da 
katkı sağlamak isterseniz benimle ömer sarıtemur#6264 hesabımla Discord uygulamasından, mail için omersaritemur3056@gmail.com üzerinden iletişime geçebilirsiniz. :)