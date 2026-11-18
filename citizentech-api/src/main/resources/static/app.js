"use strict";

/* ---------- Configuração dos recursos (uma tabela do banco por aba) ---------- */

const ZONAS = ["Norte", "Sul", "Leste", "Oeste", "Centro"];
const FAIXAS = ["PICO_AM", "ENTREPICO", "PICO_PM", "NOITE"];
const CATEGORIAS = ["LOTACAO", "ATRASO", "FREQUENCIA", "SEGURANCA", "OUTRO"];
const NIVEIS = ["BAIXO", "MEDIO", "ALTO", "CRITICO"];
const STATUS = ["ABERTO", "EM_ANALISE", "RESOLVIDO"];

const RECURSOS = {
  regioes: {
    titulo: "Regiões",
    sub: "Bairros e distritos atendidos pelas linhas.",
    endpoint: "/regioes",
    id: "idRegiao",
    singular: "região",
    campos: [
      { nome: "nome", rotulo: "Nome", tipo: "texto", max: 80 },
      { nome: "zona", rotulo: "Zona", tipo: "select", opcoes: ZONAS },
      { nome: "distanciaTerminalKm", rotulo: "Distância do terminal (km)", tipo: "numero", passo: "0.1", min: 0 },
      { nome: "rendaMedia", rotulo: "Renda média (R$)", tipo: "numero", passo: "0.01", min: 0 },
      { nome: "populacao", rotulo: "População", tipo: "numero", passo: "1", min: 1 },
    ],
  },
  linhas: {
    titulo: "Linhas",
    sub: "Linhas de ônibus, frota e intervalo entre veículos.",
    endpoint: "/linhas",
    id: "idLinha",
    singular: "linha",
    trajeto: true,
    campos: [
      { nome: "codigo", rotulo: "Código", tipo: "texto", max: 10, dica: "mínimo 4 caracteres, ex.: 3001-10" },
      { nome: "nome", rotulo: "Nome", tipo: "texto", max: 100 },
      { nome: "idRegiao", rotulo: "Região", tipo: "ref", ref: "regioes" },
      { nome: "frotaAtual", rotulo: "Frota atual (ônibus)", tipo: "numero", passo: "1", min: 0 },
      { nome: "intervaloMedioMin", rotulo: "Intervalo médio (min)", tipo: "numero", passo: "0.1", min: 0.1 },
    ],
  },
  pontos: {
    titulo: "Pontos",
    sub: "Pontos de ônibus e terminais.",
    endpoint: "/pontos",
    id: "idPonto",
    singular: "ponto",
    campos: [
      { nome: "nome", rotulo: "Nome", tipo: "texto", max: 100 },
      { nome: "endereco", rotulo: "Endereço", tipo: "texto", max: 150 },
      { nome: "latitude", rotulo: "Latitude", tipo: "numero", passo: "0.000001", min: -90, maxNum: 90 },
      { nome: "longitude", rotulo: "Longitude", tipo: "numero", passo: "0.000001", min: -180, maxNum: 180 },
      { nome: "idRegiao", rotulo: "Região", tipo: "ref", ref: "regioes" },
    ],
  },
  medicoes: {
    titulo: "Medições",
    sub: "Tempo de viagem, lotação e intervalo medidos em campo.",
    endpoint: "/medicoes",
    id: "idMedicao",
    singular: "medição",
    campos: [
      { nome: "idLinha", rotulo: "Linha", tipo: "ref", ref: "linhas" },
      { nome: "idPonto", rotulo: "Ponto", tipo: "ref", ref: "pontos" },
      { nome: "dtMedicao", rotulo: "Data e hora", tipo: "datahora" },
      { nome: "faixaHorario", rotulo: "Faixa de horário", tipo: "select", opcoes: FAIXAS },
      { nome: "tempoViagemMin", rotulo: "Tempo de viagem (min)", tipo: "numero", passo: "1", min: 1 },
      { nome: "lotacaoPct", rotulo: "Lotação (%)", tipo: "numero", passo: "1", min: 0, maxNum: 100 },
      { nome: "intervaloMin", rotulo: "Intervalo (min)", tipo: "numero", passo: "0.1", min: 0.1 },
    ],
  },
  relatos: {
    titulo: "Relatos",
    sub: "O que os moradores contam sobre o dia a dia no ônibus.",
    endpoint: "/relatos",
    id: "idRelato",
    singular: "relato",
    campos: [
      { nome: "idRegiao", rotulo: "Região", tipo: "ref", ref: "regioes" },
      { nome: "idLinha", rotulo: "Linha", tipo: "ref", ref: "linhas", opcional: true, dica: "opcional: deixe vazio se for da região toda" },
      { nome: "dtRelato", rotulo: "Data e hora", tipo: "datahora" },
      { nome: "categoria", rotulo: "Categoria", tipo: "select", opcoes: CATEGORIAS },
      { nome: "descricao", rotulo: "Descrição", tipo: "area", max: 300 },
      { nome: "tempoPerdidoMin", rotulo: "Tempo perdido (min)", tipo: "numero", passo: "1", min: 0, opcional: true, dica: "opcional" },
    ],
  },
  alertas: {
    titulo: "Alertas",
    sub: "Avisos sobre linhas que precisam de atenção.",
    endpoint: "/alertas",
    id: "idAlerta",
    singular: "alerta",
    campos: [
      { nome: "idLinha", rotulo: "Linha", tipo: "ref", ref: "linhas" },
      { nome: "idMedicao", rotulo: "Medição", tipo: "ref", ref: "medicoes", opcional: true, dica: "opcional: vazio = alerta preventivo" },
      { nome: "dtAlerta", rotulo: "Data e hora", tipo: "datahora" },
      { nome: "nivel", rotulo: "Nível", tipo: "select", opcoes: NIVEIS },
      { nome: "mensagem", rotulo: "Mensagem", tipo: "area", max: 200 },
      { nome: "status", rotulo: "Status", tipo: "select", opcoes: STATUS, padrao: "ABERTO" },
    ],
  },
};

/* Como mostrar cada registro quando ele aparece em uma lista de escolha ou em outra tabela */
const ROTULO_REF = {
  regioes: (r) => r.nome,
  linhas: (l) => `${l.codigo} - ${l.nome}`,
  pontos: (p) => p.nome,
  medicoes: (m) => `#${m.idMedicao} em ${formatarData(m.dtMedicao)}`,
};
const ID_REF = { regioes: "idRegiao", linhas: "idLinha", pontos: "idPonto", medicoes: "idMedicao" };

const cache = { regioes: [], linhas: [], pontos: [], medicoes: [] };

/* ---------- Utilitários ---------- */

function el(tag, atributos = {}, ...filhos) {
  const no = document.createElement(tag);
  for (const [k, v] of Object.entries(atributos)) {
    if (v === false || v == null) continue;
    if (k === "class") no.className = v;
    else if (k.startsWith("on")) no.addEventListener(k.slice(2), v);
    else no.setAttribute(k, v === true ? "" : v);
  }
  for (const f of filhos.flat()) {
    if (f == null) continue;
    no.append(f.nodeType ? f : document.createTextNode(String(f)));
  }
  return no;
}

function formatarData(iso) {
  if (!iso) return "";
  const d = new Date(iso);
  return isNaN(d) ? iso : d.toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" });
}

function formatarNumero(n) {
  return new Intl.NumberFormat("pt-BR", { maximumFractionDigits: 2 }).format(n);
}

async function api(caminho, opcoes = {}) {
  const resp = await fetch(caminho, {
    headers: { "Content-Type": "application/json" },
    ...opcoes,
  });
  if (!resp.ok) {
    let msg = `Erro ${resp.status}`;
    try { msg = (await resp.json()).mensagem || msg; } catch (_) { /* sem corpo JSON */ }
    throw new Error(msg);
  }
  return resp.status === 204 ? null : resp.json();
}

let temporizadorAviso;
function avisar(texto, erro = false) {
  const aviso = document.getElementById("aviso");
  aviso.textContent = texto;
  aviso.className = erro ? "aviso erro-aviso" : "aviso";
  aviso.hidden = false;
  clearTimeout(temporizadorAviso);
  temporizadorAviso = setTimeout(() => { aviso.hidden = true; }, 5000);
}

async function carregarRefs() {
  const [regioes, linhas, pontos, medicoes] = await Promise.all([
    api("/regioes"), api("/linhas"), api("/pontos"), api("/medicoes"),
  ]);
  Object.assign(cache, { regioes, linhas, pontos, medicoes });
}

function rotuloDoId(ref, id) {
  if (id == null) return "—";
  const achado = cache[ref].find((x) => x[ID_REF[ref]] === id);
  return achado ? ROTULO_REF[ref](achado) : `#${id}`;
}

function textoDaCelula(campo, valor) {
  if (valor == null || valor === "") return "—";
  if (campo.tipo === "ref") return rotuloDoId(campo.ref, valor);
  if (campo.tipo === "datahora") return formatarData(valor);
  if (campo.tipo === "numero") return formatarNumero(valor);
  return valor;
}

/* ---------- Navegação ---------- */

const ABAS = [{ chave: "ranking", titulo: "Ranking de linhas" }, ...Object.entries(RECURSOS).map(([chave, r]) => ({ chave, titulo: r.titulo }))];

function montarAbas(ativa) {
  const nav = document.getElementById("abas");
  nav.replaceChildren(...ABAS.map((a) =>
    el("button", {
      type: "button",
      "aria-current": a.chave === ativa ? "page" : false,
      onclick: () => { location.hash = a.chave; },
    }, a.titulo)));
}

async function mostrar() {
  const chave = location.hash.slice(1) || "ranking";
  const principal = document.getElementById("conteudo");
  montarAbas(chave);
  principal.replaceChildren(el("p", { class: "sub" }, "Carregando..."));
  try {
    if (chave === "ranking") await telaRanking(principal);
    else if (RECURSOS[chave]) await telaTabela(principal, chave);
    else location.hash = "ranking";
  } catch (e) {
    principal.replaceChildren(el("p", { class: "erro", role: "alert" }, `Não foi possível carregar: ${e.message}`));
  }
}

/* ---------- Tela: ranking de linhas ---------- */

async function telaRanking(principal) {
  const [ranking, relatos, alertas] = await Promise.all([
    api("/linhas/ranking"), api("/relatos"), api("/alertas"),
  ]);

  const abertos = alertas.filter((a) => a.status === "ABERTO").length;
  const minutosPerdidos = relatos.reduce((soma, r) => soma + (r.tempoPerdidoMin || 0), 0);
  const acima90 = ranking.filter((r) => r.tempoMedioMin > 90).length;
  const maximo = Math.max(120, ...ranking.map((r) => r.tempoMedioMin));

  const cards = el("div", { class: "cards" },
    card(acima90, "linhas com mais de 90 min no pico"),
    card(abertos, "alertas abertos"),
    card(relatos.length, "relatos de moradores"),
    card(minutosPerdidos, "minutos perdidos relatados"));

  const lista = ranking.length === 0
    ? el("p", { class: "vazio" }, "Ainda não há medições de pico para montar o ranking.")
    : el("ol", { class: "rank" }, ranking.map((r, i) =>
        el("li", {},
          el("div", { class: "linha1" },
            el("span", {}, `${i + 1}º `, el("span", { class: "cod" }, r.codigo), ` ${r.nome}`),
            r.tempoMedioMin > 90 ? el("span", { class: "critico" }, "Acima de 90 min") : null),
          el("p", { class: "detalhe" },
            `Tempo médio de viagem no pico: ${formatarNumero(r.tempoMedioMin)} min · ` +
            `Lotação média: ${formatarNumero(r.lotacaoMediaPct)}% · ${r.totalMedicoes} medição(ões)`),
          el("div", { class: "barra", role: "img", "aria-label": `${formatarNumero(r.tempoMedioMin)} minutos` },
            el("span", { style: `width:${Math.min(100, (r.tempoMedioMin / maximo) * 100)}%` })))));

  principal.replaceChildren(
    el("h2", {}, "Onde reforçar a frota primeiro"),
    el("p", { class: "sub" }, "Linhas ordenadas pelo maior tempo médio de viagem nos horários de pico (manhã e tarde)."),
    cards, lista);
}

function card(numero, rotulo) {
  return el("div", { class: "card" }, el("div", { class: "num" }, formatarNumero(numero)), el("div", { class: "rot" }, rotulo));
}

/* ---------- Tela: tabela com cadastro, edição e exclusão ---------- */

async function telaTabela(principal, chave) {
  const rec = RECURSOS[chave];
  const [registros] = await Promise.all([api(rec.endpoint), carregarRefs()]);

  const topo = el("div", { class: "barra-topo" },
    el("div", {}, el("h2", {}, rec.titulo), el("p", { class: "sub" }, rec.sub)),
    el("button", { type: "button", onclick: () => abrirFormulario(chave, null) }, `Cadastrar ${rec.singular}`));

  let conteudo;
  if (registros.length === 0) {
    conteudo = el("p", { class: "vazio" }, `Nenhum registro de ${rec.titulo.toLowerCase()} ainda.`);
  } else {
    conteudo = el("div", { class: "tabela-wrap" },
      el("table", {},
        el("thead", {}, el("tr", {},
          el("th", {}, "ID"),
          rec.campos.map((c) => el("th", {}, c.rotulo)),
          el("th", {}, "Ações"))),
        el("tbody", {}, registros.map((reg) =>
          el("tr", {},
            el("td", {}, reg[rec.id]),
            rec.campos.map((c) => el("td", {}, textoDaCelula(c, reg[c.nome]))),
            el("td", { class: "acoes-col" },
              rec.trajeto ? el("button", { type: "button", class: "sec mini", onclick: () => abrirTrajeto(reg) }, "Trajeto") : null,
              el("button", { type: "button", class: "sec mini", onclick: () => abrirFormulario(chave, reg) }, "Editar"),
              el("button", { type: "button", class: "perigo mini", onclick: () => excluir(chave, reg) }, "Excluir")))))));
  }
  principal.replaceChildren(topo, conteudo);
}

async function excluir(chave, reg) {
  const rec = RECURSOS[chave];
  if (!confirm(`Excluir ${rec.singular} #${reg[rec.id]}? Essa ação não pode ser desfeita.`)) return;
  try {
    await api(`${rec.endpoint}/${reg[rec.id]}`, { method: "DELETE" });
    avisar(`${capitalizar(rec.singular)} excluído(a).`);
    mostrar();
  } catch (e) {
    avisar(e.message, true);
  }
}

function capitalizar(t) { return t.charAt(0).toUpperCase() + t.slice(1); }

/* ---------- Formulário genérico ---------- */

const dlgForm = document.getElementById("dlg-form");
const form = document.getElementById("form");
let formAtual = null;

function criarCampo(campo, valor) {
  const id = `campo-${campo.nome}`;
  let entrada;
  const obrigatorio = !campo.opcional;

  if (campo.tipo === "select" || campo.tipo === "ref") {
    entrada = el("select", { id, name: campo.nome, required: obrigatorio });
    entrada.append(el("option", { value: "" }, obrigatorio ? "Selecione..." : "(nenhum)"));
    const opcoes = campo.tipo === "select"
      ? campo.opcoes.map((o) => ({ valor: o, texto: o }))
      : cache[campo.ref].map((x) => ({ valor: String(x[ID_REF[campo.ref]]), texto: ROTULO_REF[campo.ref](x) }));
    opcoes.forEach((o) => entrada.append(el("option", { value: o.valor }, o.texto)));
    const inicial = valor ?? campo.padrao ?? "";
    entrada.value = String(inicial);
  } else if (campo.tipo === "area") {
    entrada = el("textarea", { id, name: campo.nome, required: obrigatorio, maxlength: campo.max });
    entrada.value = valor ?? "";
  } else if (campo.tipo === "datahora") {
    entrada = el("input", { id, name: campo.nome, type: "datetime-local", required: obrigatorio });
    entrada.value = valor ? String(valor).slice(0, 16) : "";
  } else if (campo.tipo === "numero") {
    entrada = el("input", {
      id, name: campo.nome, type: "number", required: obrigatorio,
      step: campo.passo, min: campo.min, max: campo.maxNum,
    });
    entrada.value = valor ?? "";
  } else {
    entrada = el("input", { id, name: campo.nome, type: "text", required: obrigatorio, maxlength: campo.max });
    entrada.value = valor ?? "";
  }

  return el("label", { for: id }, campo.rotulo, campo.dica ? el("small", {}, ` (${campo.dica})`) : null, entrada);
}

function abrirFormulario(chave, registro) {
  const rec = RECURSOS[chave];
  formAtual = { chave, registro };
  document.getElementById("form-titulo").textContent =
    registro ? `Editar ${rec.singular} #${registro[rec.id]}` : `Cadastrar ${rec.singular}`;
  document.getElementById("form-campos").replaceChildren(
    ...rec.campos.map((c) => criarCampo(c, registro ? registro[c.nome] : null)));
  esconderErro("form-erro");
  dlgForm.showModal();
}

function lerValor(campo, bruto) {
  const v = bruto.trim();
  if (v === "") return campo.opcional ? null : (campo.tipo === "texto" || campo.tipo === "area" ? "" : null);
  if (campo.tipo === "numero" || campo.tipo === "ref") return Number(v);
  if (campo.tipo === "datahora") return v.length === 16 ? `${v}:00` : v;
  return v;
}

form.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  if (!formAtual) return;
  const { chave, registro } = formAtual;
  const rec = RECURSOS[chave];

  const corpo = {};
  for (const campo of rec.campos) {
    corpo[campo.nome] = lerValor(campo, document.getElementById(`campo-${campo.nome}`).value);
  }

  const botao = document.getElementById("form-salvar");
  botao.disabled = true;
  esconderErro("form-erro");
  try {
    if (registro) {
      await api(`${rec.endpoint}/${registro[rec.id]}`, { method: "PUT", body: JSON.stringify(corpo) });
    } else {
      await api(rec.endpoint, { method: "POST", body: JSON.stringify(corpo) });
    }
    dlgForm.close();
    avisar(registro ? "Alterações salvas." : "Cadastro realizado.");
    mostrar();
  } catch (e) {
    mostrarErro("form-erro", e.message);
  } finally {
    botao.disabled = false;
  }
});

document.getElementById("form-cancelar").addEventListener("click", () => dlgForm.close());

function mostrarErro(id, texto) {
  const no = document.getElementById(id);
  no.textContent = texto;
  no.hidden = false;
}
function esconderErro(id) { document.getElementById(id).hidden = true; }

/* ---------- Trajeto da linha (LINHA_PONTO) ---------- */

const dlgTrajeto = document.getElementById("dlg-trajeto");
let linhaDoTrajeto = null;

async function abrirTrajeto(linha) {
  linhaDoTrajeto = linha;
  document.getElementById("trajeto-titulo").textContent = `Trajeto da linha ${linha.codigo}`;
  const seletor = document.getElementById("trajeto-ponto");
  seletor.replaceChildren(el("option", { value: "" }, "Selecione..."),
    ...cache.pontos.map((p) => el("option", { value: p.idPonto }, p.nome)));
  esconderErro("trajeto-erro");
  await recarregarTrajeto();
  dlgTrajeto.showModal();
}

async function recarregarTrajeto() {
  const lista = document.getElementById("trajeto-lista");
  try {
    const itens = await api(`/linhas/${linhaDoTrajeto.idLinha}/pontos`);
    document.getElementById("trajeto-ordem").value = itens.length + 1;
    if (itens.length === 0) {
      lista.replaceChildren(el("li", { class: "vazio" }, "Nenhum ponto neste trajeto."));
      return;
    }
    lista.replaceChildren(...itens.map((i) =>
      el("li", {},
        el("span", {}, `${i.ordem}. ${rotuloDoId("pontos", i.idPonto)}`),
        el("button", { type: "button", class: "perigo mini", onclick: () => removerDoTrajeto(i.idPonto) }, "Remover"))));
  } catch (e) {
    mostrarErro("trajeto-erro", e.message);
  }
}

async function removerDoTrajeto(idPonto) {
  try {
    await api(`/linhas/${linhaDoTrajeto.idLinha}/pontos/${idPonto}`, { method: "DELETE" });
    esconderErro("trajeto-erro");
    recarregarTrajeto();
  } catch (e) {
    mostrarErro("trajeto-erro", e.message);
  }
}

document.getElementById("trajeto-form").addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const idPonto = Number(document.getElementById("trajeto-ponto").value);
  const ordem = Number(document.getElementById("trajeto-ordem").value);
  try {
    await api(`/linhas/${linhaDoTrajeto.idLinha}/pontos`, {
      method: "POST", body: JSON.stringify({ idPonto, ordem }),
    });
    esconderErro("trajeto-erro");
    recarregarTrajeto();
  } catch (e) {
    mostrarErro("trajeto-erro", e.message);
  }
});

document.getElementById("trajeto-fechar").addEventListener("click", () => dlgTrajeto.close());

/* ---------- Status da API e início ---------- */

async function verificarApi() {
  const no = document.getElementById("status-api");
  try {
    const resp = await fetch("/health");
    const dados = await resp.json();
    if (resp.ok && dados.banco === "ok") {
      no.textContent = "API e banco no ar";
      no.className = "status ok";
    } else {
      no.textContent = "API no ar, banco indisponível";
      no.className = "status falha";
    }
  } catch (_) {
    no.textContent = "API fora do ar";
    no.className = "status falha";
  }
}

window.addEventListener("hashchange", mostrar);
verificarApi();
mostrar();
