import React from "react";
import {
  LaptopOutlined,
  NotificationOutlined,
  UserOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Breadcrumb, Layout, Menu, theme } from "antd";
import LayoutBanner from "../src/components/commons/layout/banner/LayoutBanner.container";

const { Content, Sider } = Layout;

const items2: MenuProps["items"] = [
  UserOutlined,
  LaptopOutlined,
  NotificationOutlined,
].map((icon, index) => {
  const key = String(index + 1);

  // 게시판 이름 배열
  const boardNames = ["자유 게시판", "여행지 추천", "주변 놀거리"];

  return {
    key: `sub${key}`,
    icon: React.createElement(icon),
    label: boardNames[index], // 게시판 이름

    children:
      index === 0 // 첫 번째 메뉴인 경우
        ? [
            {
              key: "1",
              label: "게시글 작성",
              onClick: () =>
                (window.location.href = "http://localhost:3000/boards/new"),
            },
            {
              key: "2",
              label: "게시글 목록",
              onClick: () =>
                (window.location.href = "http://localhost:3000/boards"),
            },
            {
              key: "3",
              label: "추천 많은 글",
              onClick: () =>
                (window.location.href = "http://example.com/recommended"),
            },
          ]
        : new Array(4).fill(null).map((_, j) => {
            const subKey = index * 4 + j + 1;
            return {
              key: subKey.toString(),
              label: `option${subKey}`,
            };
          }),
  };
});

const App: React.FC = () => {
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  return (
    <Layout>
      <Layout>
        <Sider width={200} style={{ background: colorBgContainer }}>
          <Menu
            mode="inline"
            defaultSelectedKeys={["1"]}
            defaultOpenKeys={["sub1"]}
            style={{ height: "100%", borderRight: 0 }}
            items={items2}
          />
        </Sider>
        <Layout style={{ padding: "0 24px 24px" }}>
          <Breadcrumb style={{ margin: "16px 0" }}>
            <Breadcrumb.Item>Home</Breadcrumb.Item>
            <Breadcrumb.Item>List</Breadcrumb.Item>
            <Breadcrumb.Item>App</Breadcrumb.Item>
          </Breadcrumb>
          <Content
            style={{
              padding: 24,
              margin: 0,
              minHeight: 1000,
              background: colorBgContainer,
            }}
          >
            <LayoutBanner />
            <div
              style={{
                position: "absolute",
                top: "30%",
                left: "59%",
                transform: "translate(-50%, -50%)",
                textAlign: "center",
              }}
            >
              인기 콘텐츠
            </div>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};

export default App;
