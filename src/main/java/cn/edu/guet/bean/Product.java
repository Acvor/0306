package cn.edu.guet.bean;

public class Product {
        private int id;
        private String name;
        private Float price;
        private String type;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public Float getPrice() {
            return price;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

}
