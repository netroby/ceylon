/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
void selfType<X>(SelfType<X> x, SelfType<X> y) 
        given X satisfies SelfType<X> {
    x.compareTo(y of X);
}
void selfType2<X>(SelfType2<X> x, SelfType2<X> y) {
    x.compareTo(y of X);
}
void selfTypeTest(Summable<D> d) {
    value temp = d of D;
    //value v = print("");
    //print(of Object|Nothing v);
}
