package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.OrderSaleDto;
import com.example.demo.models.CustomerEntity;
import com.example.demo.models.InvoiceEntiy;
import com.example.demo.models.LineSaleEntity;
import com.example.demo.models.OrderSaleEntity;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.InvoiceRepository;
import com.example.demo.repositories.LineSaleRepository;
import com.example.demo.repositories.OrderSaleRepository;

@Service
public class OrderSaleServiceImpl implements OrderSaleService{

	private OrderSaleRepository reposOrderSale;
	private CustomerRepository reposCustomer;
	private InvoiceRepository  reposInvoice;
	private LineSaleRepository reposLineSale;
    private ModelMapper mapper;
    
    
    @Autowired
	public OrderSaleServiceImpl(OrderSaleRepository reposOrderSale, CustomerRepository reposCustomer, InvoiceRepository reposInvoice, LineSaleRepository reposLineSale , ModelMapper mapper) {
		super();
		this.reposOrderSale = reposOrderSale;
		this.reposCustomer = reposCustomer;
		this.reposInvoice = reposInvoice;
		this.reposLineSale = reposLineSale;
		this.mapper = mapper;
	}

	@Override
	public OrderSaleDto createOrderSale(OrderSaleDto OrderSale,int id) {
		OrderSaleEntity entity=mapper.map(OrderSale, OrderSaleEntity.class);
		CustomerEntity custEntity =reposCustomer.findById(id).get();
		entity.setCustomer(custEntity);
		InvoiceEntiy invoiceEntity = entity.getInvoice();
		InvoiceEntiy invoiceEntityInBd =reposInvoice.save(invoiceEntity);
		entity.setInvoice(invoiceEntityInBd);
		
		OrderSaleEntity newEntity= reposOrderSale.save(entity);

		return mapper.map(newEntity, OrderSaleDto.class);
	}

	@Override
	public List<OrderSaleDto> getAllOrderSales() {
		 List<OrderSaleDto>listOrderSaleDto =new ArrayList<>();
	        List<OrderSaleEntity>listOrderSaleEntity=reposOrderSale.findAll();
	        for (OrderSaleEntity orderEntity : listOrderSaleEntity) {
	            listOrderSaleDto.add(mapper.map(orderEntity,OrderSaleDto.class));
	        }
	                return listOrderSaleDto;
	}

	@Override
	public OrderSaleDto getOrderSaleById(int id) {
		  OrderSaleEntity entity=reposOrderSale.findById(id).get();
	        return mapper.map(entity, OrderSaleDto.class);
	    }
	

	@Override
	public OrderSaleDto modifyOrderSale(int id, OrderSaleDto OrderSale) {
            OrderSaleEntity entity=reposOrderSale.findById(id).get();
	        entity.setTotalPrice(OrderSale.getTotalPrice());
	        entity.setDate(OrderSale.getDate());
	        OrderSaleEntity newEntity=reposOrderSale.save(entity);
	        return mapper.map(newEntity, OrderSaleDto.class);
	    
	}

	@Override
	public void deleteOrderSaleById(int id) {
		reposOrderSale.deleteById(id);
				
	}
	////
	/////
	//  calcul de la commande
	/*@Override
	public float calculCommande(int numero)
	{
		float somme = 0;
		OrderSaleEntity orderEntity=reposOrderSale.findById(numero).get();
		for (LineSaleEntity line : orderEntity.getLineSales()) {
			if(line.getProduct().getQuantite() > line.getQt())
			    {
			     somme += line.getQt()*line.getProduct().getPriceVente();
			     int res = line.getProduct().getQuantite()-line.getQt();
			     line.getProduct().setQuantite(res);
			     reposLineSale.save(line);
			    }
			else
			    {
				throw new ArithmeticException("imossible de passer une commande ");
			    }
				
			
		}
		orderEntity.setTotalPrice(somme);
		reposOrderSale.save(orderEntity);
		return somme;
		
	}*/

}
