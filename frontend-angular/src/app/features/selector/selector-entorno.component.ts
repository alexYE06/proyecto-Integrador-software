import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-selector-entorno',
  standalone: true,
  template: `
    <div class="contenedor-demo">
      <div class="tarjeta-central">
        <header class="encabezado">
          <div class="insignia">PROYECTO INTEGRADOR I</div>
          <h1>SAT - CARMEN DE LA PUNTA</h1>
          <p class="subtitulo">Sistema de Alerta Silenciosa y Monitoreo de Flota</p>
        </header>

        <main class="grid-entornos">
          <!-- Opción Chofer / Móvil -->
          <div class="card-opcion chofer" (click)="irAEntorno('chofer')">
            <div class="icono-entorno">📱</div>
            <div class="badge-tag">Móvil / PWA</div>
            <h2>Entorno Conductor</h2>
            <p>Formato táctil vertical para smartphone en ruta. Botón de pánico silencioso, estado de unidad y recorrido.</p>
            <button class="btn-acceder btn-chofer">
              Iniciar Móvil Chofer
              <span class="flecha">→</span>
            </button>
          </div>

          <!-- Opción Central / Operador -->
          <div class="card-opcion central" (click)="irAEntorno('central')">
            <div class="icono-entorno">🖥️</div>
            <div class="badge-tag central-tag">Consola Web</div>
            <h2>Central de Operaciones</h2>
            <p>Monitoreo integral de flota en mapa en tiempo real, gestión de alertas críticas y despacho de auxilio policial.</p>
            <button class="btn-acceder btn-central">
              Ingresar a Central
              <span class="flecha">→</span>
            </button>
          </div>
        </main>

        <footer class="pie-selector">
          <span>Carmen de la Punta S.A. &copy; 2026 • Versión Web Unificada Angular + Spring Boot</span>
        </footer>
      </div>
    </div>
  `,
  styles: [`
    .contenedor-demo {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: radial-gradient(circle at top, #1e293b 0%, #0f172a 100%);
      font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      padding: 20px;
      color: #f8fafc;
      box-sizing: border-box;
    }

    .tarjeta-central {
      max-width: 900px;
      width: 100%;
      background: rgba(30, 41, 59, 0.7);
      backdrop-filter: blur(12px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 20px;
      padding: 40px;
      box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
    }

    .encabezado {
      text-align: center;
      margin-bottom: 35px;
    }

    .insignia {
      display: inline-block;
      font-size: 11px;
      font-weight: 700;
      letter-spacing: 1.5px;
      color: #38bdf8;
      background: rgba(56, 189, 248, 0.12);
      padding: 5px 14px;
      border-radius: 9999px;
      margin-bottom: 12px;
      border: 1px solid rgba(56, 189, 248, 0.25);
    }

    h1 {
      font-size: 28px;
      font-weight: 800;
      letter-spacing: -0.5px;
      margin: 0 0 8px 0;
      background: linear-gradient(to right, #ffffff, #94a3b8);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .subtitulo {
      font-size: 14px;
      color: #94a3b8;
      margin: 0;
    }

    .grid-entornos {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 25px;
      margin-bottom: 30px;
    }

    @media (max-width: 768px) {
      .grid-entornos {
        grid-template-columns: 1fr;
      }
    }

    .card-opcion {
      background: rgba(15, 23, 42, 0.6);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 16px;
      padding: 28px;
      cursor: pointer;
      transition: all 0.25s ease;
      display: flex;
      flex-direction: column;
      position: relative;
    }

    .card-opcion:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 24px -10px rgba(0, 0, 0, 0.6);
    }

    .card-opcion.chofer:hover {
      border-color: #06b6d4;
    }

    .card-opcion.central:hover {
      border-color: #f43f5e;
    }

    .icono-entorno {
      font-size: 36px;
      margin-bottom: 12px;
    }

    .badge-tag {
      position: absolute;
      top: 24px;
      right: 24px;
      font-size: 11px;
      font-weight: 600;
      padding: 3px 10px;
      border-radius: 6px;
      background: rgba(6, 182, 212, 0.15);
      color: #22d3ee;
    }

    .badge-tag.central-tag {
      background: rgba(244, 63, 94, 0.15);
      color: #fb7185;
    }

    h2 {
      font-size: 20px;
      font-weight: 700;
      margin: 0 0 10px 0;
      color: #ffffff;
    }

    p {
      font-size: 13px;
      line-height: 1.5;
      color: #94a3b8;
      margin: 0 0 24px 0;
      flex-grow: 1;
    }

    .btn-acceder {
      width: 100%;
      padding: 12px;
      border-radius: 10px;
      border: none;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      transition: all 0.2s;
    }

    .btn-chofer {
      background: #0891b2;
      color: #ffffff;
    }

    .btn-chofer:hover {
      background: #06b6d4;
    }

    .btn-central {
      background: #e11d48;
      color: #ffffff;
    }

    .btn-central:hover {
      background: #f43f5e;
    }

    .flecha {
      transition: transform 0.2s;
    }

    .card-opcion:hover .flecha {
      transform: translateX(4px);
    }

    .pie-selector {
      text-align: center;
      font-size: 11px;
      color: #64748b;
      border-top: 1px solid rgba(255, 255, 255, 0.06);
      padding-top: 20px;
    }
  `]
})
export class SelectorEntornoComponent {
  constructor(private router: Router) {}

  irAEntorno(tipo: string) {
    if (tipo === 'central') {
      this.router.navigate(['/login-central']);
    } else {
      this.router.navigate(['/login-chofer']);
    }
  }
}
