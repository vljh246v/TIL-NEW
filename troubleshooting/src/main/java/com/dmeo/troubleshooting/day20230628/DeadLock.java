package com.dmeo.troubleshooting.day20230628;

public class DeadLock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s has bowed to me!%n", this.name, bower.getName());
            bower.bowBack(this);
        }

        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s has bowed back to me !%n", this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend alpha = new Friend("alpha");
        final Friend beta = new Friend("beta");
        new Thread(new Runnable() {
            @Override
            public void run() {
                alpha.bow(beta);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                beta.bow(alpha);
            }
        }).start();
    }
}
