/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.util.utilities.vector.impl;

import cn.Judgment.util.utilities.vector.Vector;

public class Vector3<T extends Number>
extends Vector<Number> {
    public Vector3(T x, T y, T z) {
        super(x, y, z);
    }

    public Vector2<T> toVector2() {
        return new Vector2(this.getX(), this.getY());
    }
}

