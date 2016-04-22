package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @NotNull
    @Column(name = "payment_moment", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime paymentMoment;

    @OneToOne(optional = false)
    @JoinColumn(unique = true, nullable = false)
    @Valid
    @NotNull
    private Request request;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private AppUser payer;

    @OneToOne(mappedBy = "payment")
    @JsonIgnore
    @Valid
    private Assessment assessment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ZonedDateTime getPaymentMoment() {
        return paymentMoment;
    }

    public void setPaymentMoment(ZonedDateTime paymentMoment) {
        this.paymentMoment = paymentMoment;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public AppUser getPayer() {
        return payer;
    }

    public void setPayer(AppUser appUser) {
        this.payer = appUser;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        if(payment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", paymentMoment='" + paymentMoment + "'" +
            '}';
    }
}
