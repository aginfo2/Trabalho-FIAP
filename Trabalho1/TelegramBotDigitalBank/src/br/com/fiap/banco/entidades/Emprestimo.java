package br.com.fiap.banco.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entidade que representa a tabela Emprestimo no BD
 *
 */
@Entity
@Table(name = "EMPRESTIMO", catalog = "DBBotBank")
@Cacheable(true)
public class Emprestimo implements Serializable {

	private static final long serialVersionUID = -1004540327268742079L;

	/**
	 * Id auto-gerado
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	/**
	 * Relacionamento com a conta � qual esse empr�stimo pertence
	 */
	@ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Conta conta;

	/**
	 * N�mero da parcela do empr�stimo
	 */
	@Column(name = "numero_parcela", unique = false, nullable = false, length = 1)
	private int numeroParcela;

	/**
	 * Valor da parcela do empr�stimo
	 */
	@Column(name = "valor_parcela", unique = false, nullable = false)
	private double valorParcela;

	/**
	 * Juros da parcela do empr�stimo
	 */
	@Column(name = "juros", unique = false, nullable = false)
	private double juros;

	/**
	 * Data de vencimento da parcela do empr�stimo
	 */
	@Column(name = "data_vencimento", unique = false, nullable = false)
	private LocalDate dataVencimento;

	/**
	 * Indicador se a parcela j� foi paga
	 */
	@Column(name = "parcela_paga", unique = false, nullable = false)
	private boolean parcelaPaga;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public int getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(int numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public double getJuros() {
		return juros;
	}

	public void setJuros(double juros) {
		this.juros = juros;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public boolean isParcelaPaga() {
		return parcelaPaga;
	}

	public void setParcelaPaga(boolean parcelaPaga) {
		this.parcelaPaga = parcelaPaga;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Emprestimo)) {
			return false;
		}
		Emprestimo other = (Emprestimo) obj;
		if (conta == null) {
			if (other.conta != null) {
				return false;
			}
		} else if (!conta.equals(other.conta)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
