package io.neural.micro;

import lombok.*;

import java.io.Serializable;

/**
 * The Store
 *
 * @param <K>
 * @param <V>
 * @author lry
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Store<K, V> implements Serializable {

    private static final long serialVersionUID = 367372199912627942L;

    private K key;
    private V value;

}
