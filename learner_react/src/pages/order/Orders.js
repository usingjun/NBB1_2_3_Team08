import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import styled from "styled-components";

const Order_Url = "http://localhost:8080/order";

const Orders = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const memberId = localStorage.getItem("memberId");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchOrders = async () => {
            setLoading(true);
            if (!memberId) {
                setError("로그인이 필요합니다.");
                setLoading(false);
                return;
            }
            try {
                const response = await axios.get(`${Order_Url}/list/${memberId}`, { withCredentials: true });
                if (Array.isArray(response.data)) {
                    setOrders(response.data);
                } else {
                    console.error('주문 응답이 배열이 아닙니다:', response.data);
                    setError("주문 목록을 가져오는 데 실패했습니다.");
                    setOrders([]);
                }
            } catch (error) {
                console.error("주문 가져오는 중 오류 발생:", error);
                setError("주문 목록을 가져오는 데 실패했습니다.");
                setOrders([]);
            } finally {
                setLoading(false);
            }
        };
        fetchOrders();
    }, [memberId]);

    const handleUpdateClick = (orderId) => {
        navigate(`/orders/update/${orderId}`);
    };

    const handleDeleteClick = (orderId) => {
        if (window.confirm("정말로 이 주문을 삭제하시겠습니까?")) {
            navigate(`/orders/delete/${orderId}`);
        }
    };

    const handlePurchase = async (orderId) => {
        const memberId = localStorage.getItem("memberId");
        try {
            const response = await axios.post(`${Order_Url}/purchase/${orderId}`, { orderId, memberId }, { withCredentials: true });
            alert("결제가 완료되었습니다. 주문 ID: " + response.data.orderId);
            // 성공적으로 결제 후 해당 주문 제거
            await axios.delete(`${Order_Url}/${orderId}`, { withCredentials: true });
            window.location.reload();

        } catch (error) {
            console.error("결제 중 오류 발생:", error);
            alert("결제에 실패했습니다.");
        }
    };

    if (loading) return <LoadingMessage>로딩 중...</LoadingMessage>;
    if (error) return <ErrorMessage>{error}</ErrorMessage>;

    return (
        <OrderList>
            <Header>주문 목록</Header>
            <Link to="/orders/create">
                <StyledButton primary>주문 생성</StyledButton>
            </Link>
            {orders.length > 0 ? (
                orders.map(order => (
                    <OrderItem key={order.orderId}>
                        <p>주문 ID: <strong>{order.orderId}</strong></p>
                        <p>주문 날짜: <strong>{new Date(order.createdDate).toLocaleDateString()}</strong></p>
                        <p>강의: <strong>{order.orderItemDTOList.map(item => item.courseName).join(', ')}</strong></p>
                        <p>총 금액: <strong>{order.totalPrice} 원</strong></p>
                        <ButtonContainer>
                            <StyledButton onClick={() => handleUpdateClick(order.orderId)} update>수정</StyledButton>
                            <Link to={`/orders/${order.orderId}`}>
                                <StyledButton>상세보기</StyledButton>
                            </Link>
                            <StyledButton onClick={() => handlePurchase(order.orderId)} purchase>구매</StyledButton>
                            <StyledButton onClick={() => handleDeleteClick(order.orderId)} delete>삭제</StyledButton>
                        </ButtonContainer>
                    </OrderItem>
                ))
            ) : (
                <p>주문이 없습니다.</p>
            )}
        </OrderList>
    );
};

// 스타일 컴포넌트들
const OrderList = styled.div`
    max-width: 800px;
    margin: 0 auto;
    padding: 2rem;
    background: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
`;

const Header = styled.h2`
    text-align: center;
    color: #333;
    margin-bottom: 1.5rem;
`;

const LoadingMessage = styled.p`
    text-align: center;
    color: #007bff;
`;

const ErrorMessage = styled.p`
    text-align: center;
    color: red;
    font-weight: bold;
`;

const OrderItem = styled.div`
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin-bottom: 1rem;
    background-color: #fff;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: space-between;
    margin-top: 1rem;
`;

const StyledButton = styled.button`
    padding: 0.5rem 1rem;
    background-color: ${props =>
            props.primary ? "#007bff" :
                    props.delete ? "#dc3545" :
                            props.purchase ? "#28a745" :
                                    props.update ? "#ffc107" : "#6c757d"
    };
    color: ${props => props.purchase ? "white" : "white"};
    border: ${props => props.purchase ? "none" : "none"};
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
        background-color: ${props =>
                props.primary ? "#0056b3" :
                        props.delete ? "#c82333" :
                                props.purchase ? "#218838" :
                                        props.update ? "#e0a800" : "#5a6268"
        };
    }
`;

export default Orders;
